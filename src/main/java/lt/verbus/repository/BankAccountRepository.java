package lt.verbus.repository;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.Bank;
import lt.verbus.model.BankAccount;
import lt.verbus.model.CardType;
import lt.verbus.model.Transaction;
import lt.verbus.model.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BankAccountRepository extends GenericRepository<BankAccount> {

    private final String SELECT_ALL_BELONGING_TO = ("SELECT * FROM " + super.databaseTableName + " WHERE ");

    public BankAccountRepository() throws SQLException, IOException {
        super("bank_account");
    }

    public BankAccount findByIban(String iban) throws SQLException, EntityNotFoundException, IOException {
        return super.findByUniqueCode("iban", iban);
    }

    public List<BankAccount> findAllBelongingToUser(User user) throws SQLException, IOException {
        String query = SELECT_ALL_BELONGING_TO + "user_id = " + user.getId();
        ResultSet table = statement.executeQuery(query);
        return convertTableToList(table);
    }

    public List<BankAccount> findAllBelongingToBank(Bank bank) throws SQLException, IOException {
        String query = SELECT_ALL_BELONGING_TO + "bank_id = " + bank.getId();
        ResultSet table = statement.executeQuery(query);
        return convertTableToList(table);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) throws SQLException, EntityNotFoundException, IOException {
        String query = String.format("INSERT INTO \"%s\" " +
                        "(bank_id, iban, card_type, user_id, balance) " +
                        "VALUES (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\")",
                super.databaseTableName,
                bankAccount.getBank().getId(),
                bankAccount.getIban(),
                bankAccount.getCardType().toString(),
                bankAccount.getHolder().getId(),
                bankAccount.getBalance());
        statement.execute(query);
        return findByIban(bankAccount.getIban());
    }

    @Override
    public void update(BankAccount bankAccount) throws SQLException {
        String query = String.format("UPDATE %s SET " +
                        "bank_id = %d, " +
                        "iban = \"%s\", " +
                        "card_type = \"%s\", " +
                        "user_id = %d, " +
                        "balance = %.2f " +
                        "WHERE id = %d",
                super.databaseTableName,
                bankAccount.getBank().getId(),
                bankAccount.getIban(),
                bankAccount.getCardType().toString(),
                bankAccount.getHolder().getId(),
                bankAccount.getBalance(),
                bankAccount.getId());
        statement.executeUpdate(query);
    }

    public boolean updateByTransaction(Transaction transaction) throws SQLException {
        BankAccount sender = transaction.getSender();
        BankAccount receiver = transaction.getReceiver();
        Double amount = transaction.getAmount();
        connection.setAutoCommit(false);
        try {
            String queryForSender = String.format("UPDATE " +  super.databaseTableName +
                            " SET balance = balance - %.2f WHERE id = %d",
                    amount, sender.getId()
            );
            String queryForReceiver = String.format("UPDATE " +  super.databaseTableName +
                            " SET balance = balance + %.2f WHERE id = %d",
                    amount, receiver.getId()
            );
            statement.executeUpdate(queryForSender);
            statement.executeUpdate(queryForReceiver);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
        return true;
    }

    protected BankAccount convertTableToObject(ResultSet table) throws SQLException, IOException {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(table.getInt("id"));
        bankAccount.setIban(table.getString("iban"));
        bankAccount.setBalance(table.getDouble("balance"));
        bankAccount.setCardType(CardType
                .valueOf(table.getString("card_type").toUpperCase()));

        BankRepository bankRepository = new BankRepository();
        bankAccount.setBank(bankRepository.findById(table.getInt("bank_id")));

        UserRepository userRepository = new UserRepository();
        bankAccount.setHolder(userRepository.findById(table.getInt("user_id")));

        return bankAccount;
    }


}
