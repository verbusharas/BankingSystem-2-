package lt.verbus.services;


import lt.verbus.exception.InsufficientFundsException;
import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.Bank;
import lt.verbus.model.BankAccount;
import lt.verbus.model.CardType;
import lt.verbus.model.Transaction;
import lt.verbus.model.User;
import lt.verbus.repository.BankAccountRepository;
import lt.verbus.repository.ConnectionPool;
import lt.verbus.repository.CreditRepository;
import lt.verbus.repository.SqlDialect;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private CreditRepository creditRepository;
    private CreditService creditService;

    public BankAccountService(BankAccountRepository bankAccountRepository) throws IOException, SQLException {
        this.bankAccountRepository = bankAccountRepository;
        creditRepository = new CreditRepository(ConnectionPool.getInstance().getConnection());
        creditService = new CreditService(creditRepository);
    }

    public List<BankAccount> findAll() throws SQLException {
        return bankAccountRepository.findAll();
    }

    public BankAccount findByIban(String iban) throws SQLException, EntityNotFoundException {
        return bankAccountRepository.findByIban(iban);
    }

    public List<BankAccount> findAllBelongingToUser(User user) throws SQLException {
        return bankAccountRepository.findAllBelongingToUser(user);
    }

    public List<BankAccount> findAllBelongingToBank(Bank bank) throws SQLException {
        return bankAccountRepository.findAllBelongingToBank(bank);
    }

    public BankAccount findById(Long id) throws SQLException {
        return bankAccountRepository.findById(id);
    }

    public BankAccount save(BankAccount bankAccount) throws SQLException, EntityNotFoundException {
        return bankAccountRepository.save(bankAccount);
    }

    public void update(BankAccount bankAccount) throws SQLException {
        bankAccountRepository.update(bankAccount);
    }

    public void delete(Long id) throws SQLException {
        bankAccountRepository.delete(id);
    }

    public String generateIban() {
        Random rand = new Random();
        String iban = "LT";
        iban += (rand.nextInt(9-1) + 1) + "0";
        iban += (rand.nextInt(9999-1000) + 1000);
        iban += (rand.nextInt(999-100) + 100);
        iban += (rand.nextInt(999-100) + 100);
        iban += (rand.nextInt(999-100) + 100);
        iban += (rand.nextInt(99-10) + 10);
        return iban;
    }

    public boolean transferFunds(Transaction transaction) throws SQLException, InsufficientFundsException, EntityNotFoundException {
        validateIfSufficientFunds(transaction);
        if (bankAccountRepository.updateByTransaction(transaction)) {
            creditService.updateCredits(transaction);
            return true;
        } else return false;
    }

    public void addFunds(Transaction transaction) throws SQLException, EntityNotFoundException {
        double amount = transaction.getAmount();
        BankAccount targetBankAccount = transaction.getReceiver();
        creditService.updateCredits(transaction);
        targetBankAccount.setBalance(targetBankAccount.getBalance() + amount);
        bankAccountRepository.update(targetBankAccount);
    }

    public void subtractFunds(Transaction transaction) throws InsufficientFundsException, SQLException, EntityNotFoundException {
        validateIfSufficientFunds(transaction);
        double amount = transaction.getAmount();
        BankAccount sourceBankAccount = transaction.getSender();
        double currentBalance = sourceBankAccount.getBalance();
        creditService.updateCredits(transaction);
        sourceBankAccount.setBalance(currentBalance - amount);
        bankAccountRepository.update(sourceBankAccount);
    }

    private void validateIfSufficientFunds(Transaction transaction) throws InsufficientFundsException {
        double sourceBalance = transaction.getSender().getBalance();
        double amount = transaction.getAmount();
        double difference = sourceBalance - amount;
        if (difference < 0 && transaction.getSender().getCardType().equals(CardType.DEBIT)) {
            throw new InsufficientFundsException();
        }
    }
}
