package lt.verbus.services;

import lt.verbus.config.CreditInterest;
import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.BankAccount;
import lt.verbus.model.Credit;
import lt.verbus.model.Transaction;
import lt.verbus.model.User;
import lt.verbus.repository.CreditRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class CreditService {

    private final CreditRepository creditRepository;

    public CreditService() throws IOException, SQLException {
        creditRepository = new CreditRepository();
    }

    public List<Credit> findAll() throws SQLException, IOException {
        return creditRepository.findAll();
    }

    public Credit findByBankAccount(BankAccount bankAccount) throws SQLException, IOException {
        return creditRepository.findByBankAccount(bankAccount);
    }

    public List<Credit> findAllByDebtor(User user) throws SQLException, IOException {
        return creditRepository.findAllByDebtor(user);
    }

    public Credit findById(long id) throws SQLException, IOException {
        return creditRepository.findById(id);
    }

    public Credit save(Credit credit) throws SQLException, IOException {
        return creditRepository.save(credit);
    }

    public void update(Credit credit) throws SQLException {
        creditRepository.update(credit);
    }

    public void delete(Long id) throws SQLException {
        creditRepository.delete(id);
    }

    public void updateCredits(Transaction transaction) throws SQLException, EntityNotFoundException, IOException {
        updateSenderCredits(transaction);
        updateReceiverCredits(transaction);
    }

    private void updateSenderCredits(Transaction transaction) throws SQLException, IOException {
        BankAccount sender = transaction.getSender();
        double transferedAmount = transaction.getAmount();
        Timestamp timeOfEvent = transaction.getTimestamp();

        if (sender != null) {
            double senderInitialBalance = sender.getBalance();
            double senderDifference = senderInitialBalance - transferedAmount;
            if (senderDifference < 0) {
                Credit senderCredit = findByBankAccount(sender);
                senderCredit.setAmount(senderDifference);
                if (senderCredit.getCreditStartTime() == null) {
                    senderCredit.setCreditStartTime(timeOfEvent);
                }
                update(senderCredit);
            }
        }
    }

    private void updateReceiverCredits(Transaction transaction) throws SQLException, IOException {
        BankAccount receiver = transaction.getReceiver();
        double transferedAmount = transaction.getAmount();
        if (receiver != null) {
            double receiverInitialBalance = receiver.getBalance();

            if (receiverInitialBalance < 0) {
                Credit receiverCredit = findByBankAccount(receiver);
                double creditCost = 0;
                double receiverBorrowedAmount = receiverCredit.getAmount();
                LocalDateTime timeOfBorrow = receiverCredit.getCreditStartTime().toLocalDateTime();

                if (timeOfBorrow.plusMonths(1).isBefore(LocalDateTime.now())) {
                    creditCost = receiverBorrowedAmount * CreditInterest.PERCENT / 100;
                }

                if (receiverBorrowedAmount + transferedAmount >= 0) {
                    receiverCredit.setAmount(creditCost);
                }
                update(receiverCredit);
            }
        }
    }

}
