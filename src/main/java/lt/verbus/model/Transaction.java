package lt.verbus.model;

import java.sql.Timestamp;

public class Transaction {

    private long id;
    private BankAccount sender;
    private BankAccount receiver;
    private double amount;
    private Timestamp timestamp;

    public Transaction() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BankAccount getSender() {
        return sender;
    }

    public void setSender(BankAccount sender) {
        this.sender = sender;
    }

    public BankAccount getReceiver() {
        return receiver;
    }

    public void setReceiver(BankAccount receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("[TRANSACTION] %s | (%20s) %14s  -> | %-14s (%20s) |  %.2f EUR ",
                timestamp.toString(),
                sender == null? "" : sender.getIban(),
                sender == null? "TOP UP (+)" : sender.getHolder().getFullName(),
                receiver == null? "(-) WITHDRAW" : receiver.getHolder().getFullName(),
                receiver == null? "" : receiver.getIban(),
                getAmount());
    }
}
