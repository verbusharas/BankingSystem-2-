package lt.verbus.services;

import lt.verbus.exception.InsufficientFundsException;
import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.BankAccount;
import lt.verbus.model.Transaction;
import lt.verbus.repository.TransactionRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {

    private BankAccountService bankAccountService;
    private TransactionRepository transactionRepository;

    public TransactionService() throws IOException, SQLException {
        bankAccountService = new BankAccountService();
        transactionRepository = new TransactionRepository();
    }

    public List<Transaction> findAll() throws SQLException, IOException {
        return transactionRepository.findAll();
    }

    public List<Transaction> findAllByBankAccount(BankAccount bankAccount) throws SQLException, IOException {
        return transactionRepository.findAllByBankAccount(bankAccount);
    }

    public Transaction findByTimestamp(String timestampString) throws SQLException, EntityNotFoundException, IOException {
        return transactionRepository.findByTimestamp(timestampString);
    }

    public Transaction findById(Long id) throws SQLException, IOException {
        return transactionRepository.findById(id);
    }


    public void update(Transaction transaction) throws SQLException {
        transactionRepository.update(transaction);
    }

    public void delete(Long id) throws SQLException {
        transactionRepository.delete(id);
    }

    public void execute(Transaction transaction) throws InsufficientFundsException, SQLException, EntityNotFoundException, IOException {
        //evaluates if operation is transfer/top-up/withdraw and selects method from repository accordingly
        if(transaction.getSender() == null || transaction.getReceiver() == null) {
            if (transaction.getSender() == null) {
                bankAccountService.addFunds(transaction);
                transactionRepository.saveAsTopUp(transaction);
            }
            if (transaction.getReceiver() == null) {
                bankAccountService.subtractFunds(transaction);
                transactionRepository.saveAsWithdraw(transaction);
            }
        } else {
            if (bankAccountService.transferFunds(transaction)) {
                transactionRepository.save(transaction);
            }
        }
    }

}
