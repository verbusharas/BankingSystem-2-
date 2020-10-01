package lt.verbus.repository;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.BankAccount;
import lt.verbus.model.Transaction;

import java.sql.*;
import java.util.List;

public class TransactionRepository extends GenericRepository<Transaction>{


    public TransactionRepository(Connection connection) throws SQLException {
        super(connection, "transaction");
    }

    public List<Transaction> findAllByBankAccount(BankAccount bankAccount) throws SQLException {
        String query = String.format("SELECT * FROM transaction " +
                        "WHERE sender_bank_account_id = %d " +
                        "OR receiver_bank_account_id = %d",
        bankAccount.getId(), bankAccount.getId());
        ResultSet table = statement.executeQuery(query);
        return convertTableToList(table);
    }

    public Transaction findByTimestamp(String timestampString) throws SQLException, EntityNotFoundException {
        return super.findByUniqueCode("timestamp", timestampString);
    }

    @Override
    public Transaction save(Transaction transaction) throws SQLException, EntityNotFoundException {
        String query = String.format("INSERT INTO transaction " +
                        "(sender_bank_account_id, receiver_bank_account_id, amount, timestamp) " +
                        "VALUES (%d, %d, %.2f, \"%s\")",
                transaction.getSender().getId(), transaction.getReceiver().getId(),
                transaction.getAmount(), transaction.getTimestamp().toString());
        statement.execute(query);
        // TODO: return object with sql generated id according to other repository examples
        return null;
    }

    public Transaction saveAsTopUp(Transaction transaction) throws SQLException {
        String query = String.format("INSERT INTO transaction " +
                        "(receiver_bank_account_id, amount, timestamp) " +
                        "VALUES (%d, %.2f, \"%s\")",
                transaction.getReceiver().getId(),
                transaction.getAmount(), transaction.getTimestamp().toString());
        statement.execute(query);
        // TODO: return object with sql generated id according to other repository examples
        return null;
    }

    public Transaction saveAsWithdraw(Transaction transaction) throws SQLException {
        String query = String.format("INSERT INTO transaction " +
                        "(sender_bank_account_id, amount, timestamp) " +
                        "VALUES (%d, %.2f, \"%s\")",
                transaction.getSender().getId(),
                transaction.getAmount(),
                transaction.getTimestamp().toString());
        statement.execute(query);
        // TODO: return object with sql generated id according to other repository examples
        return null;
    }

    @Override
    public void update(Transaction transaction) throws SQLException {
        String query = String.format("UPDATE transaction SET " +
                        "sender_bank_account_id = %d, " +
                        "receiver_bank_account_id = %d, " +
                        "amount = %.2f, " +
                        "timestamp = \"%s\" " +
                        "WHERE id = %d",
                transaction.getSender().getId(),
                transaction.getReceiver().getId(),
                transaction.getAmount(),
                transaction.getTimestamp().toString(),
                transaction.getId());
        statement.executeUpdate(query);
    }

    @Override
    public Transaction convertTableToObject(ResultSet table) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(table.getInt("id"));
        transaction.setTimestamp(table.getTimestamp("timestamp"));
        transaction.setAmount(table.getDouble("amount"));

        BankAccountRepository bankAccountRepository = new BankAccountRepository(connection);

        long senderAccountId = table.getInt("sender_bank_account_id");
        long receiverAccountId = table.getInt("receiver_bank_account_id");

        if (senderAccountId > 0) {
            transaction.setSender(bankAccountRepository
                    .findById(senderAccountId));
        } else {
            transaction.setSender(null);
        }
        if (receiverAccountId > 0) {
            transaction.setReceiver(bankAccountRepository
                    .findById(receiverAccountId));
        } else {
            transaction.setReceiver(null);
        }
        return transaction;
    }
}
