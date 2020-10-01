package lt.verbus.services;

import lt.verbus.config.CreditInterest;
import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.BankAccount;
import lt.verbus.model.Credit;
import lt.verbus.model.Transaction;
import lt.verbus.model.User;
import lt.verbus.repository.CreditRepository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class CreditService {

    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public List<Credit> findAll() throws SQLException {
        return creditRepository.findAll();
    }

    public Credit findByBankAccount(BankAccount bankAccount) throws SQLException, EntityNotFoundException {
        return creditRepository.findByBankAccount(bankAccount);
    }

    public List<Credit> findAllByDebtor(User user) throws SQLException, EntityNotFoundException {
        return creditRepository.findAllByDebtor(user);
    }

    public Credit findById(long id) throws SQLException {
        return creditRepository.findById(id);
    }

    public Credit save(Credit credit) throws SQLException, EntityNotFoundException {
        return creditRepository.save(credit);
    }

    public void update(Credit credit) throws SQLException {
        creditRepository.update(credit);
    }

    public void delete(Long id) throws SQLException {
        creditRepository.delete(id);
    }

    public void updateCredits(Transaction transaction) throws SQLException, EntityNotFoundException {
        BankAccount sender = transaction.getSender();
        BankAccount receiver = transaction.getReceiver();

        double transferedAmount = transaction.getAmount();
        Timestamp timeOfEvent = transaction.getTimestamp();

        // SENDER CREDIT CHECK:
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

        // RECEIVER CREDIT CHECK
        if (receiver != null) {
            double receiverInitialBalance = receiver.getBalance();
            if (receiverInitialBalance < 0) {
                System.out.println("i am here in receiver check");
                Credit receiverCredit = findByBankAccount(receiver);
                double creditCost = 0;
                double receiverBorrowedAmount = receiverCredit.getAmount();
                LocalDateTime timeOfBorrow = receiverCredit.getCreditStartTime().toLocalDateTime();
                if (timeOfBorrow.plusMonths(1).isBefore(LocalDateTime.now())) {
                    System.out.println("i am here in moth has passed");
                    creditCost = receiverBorrowedAmount * CreditInterest.PERCENT / 100;
                    System.out.println("credit cost: " + creditCost);
                }
                if (receiverBorrowedAmount + transferedAmount >= 0) {
                    System.out.println("i am here, where after transfer credit should be returned ");
                    receiverCredit.setAmount(creditCost);
                    System.out.println("receiver credit amount: " +receiverCredit.getAmount());
                }
                System.out.println(receiverCredit);
                update(receiverCredit);
            }
        }
    }
}
