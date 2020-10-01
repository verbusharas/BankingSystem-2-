package lt.verbus.model;

import java.sql.Timestamp;

public class Credit {

    private long id;
    private double amount;
    private Timestamp creditStartTime;
    private BankAccount creditedBankAccount;

    public Credit() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getCreditStartTime() {
        return creditStartTime;
    }

    public void setCreditStartTime(Timestamp creditStartTime) {
        this.creditStartTime = creditStartTime;
    }

    public BankAccount getCreditedBankAccount() {
        return creditedBankAccount;
    }

    public void setCreditedBankAccount(BankAccount creditedBankAccount) {
        this.creditedBankAccount = creditedBankAccount;
    }

    @Override
    public String toString() {
        return String.format("[CREDIT] Credited bank account : %-15s| Amount to return: %.2f | Credit taken on: %s ",
                creditedBankAccount.getIban(), getAmount(), getCreditStartTime() == null ? "not taken" : getCreditStartTime());
    }
}
