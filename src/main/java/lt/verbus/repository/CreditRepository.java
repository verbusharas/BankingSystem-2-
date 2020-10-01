package lt.verbus.repository;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.BankAccount;
import lt.verbus.model.Credit;
import lt.verbus.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CreditRepository extends GenericRepository<Credit> {

    public CreditRepository(Connection connection) throws SQLException {
        super(connection, "credit");
    }

    public List<Credit> findAllByDebtor(User user) throws SQLException, EntityNotFoundException {
        String query = String.format("SELECT * FROM credit c " +
                        "JOIN bank_account ba ON c.bank_account_id = ba.id " +
                        "JOIN user u ON ba.user_id = u.id " +
                        "WHERE u.id = %d",
                user.getId());
        ResultSet table = statement.executeQuery(query);
        return convertTableToList(table);
    }

    public Credit findByBankAccount(BankAccount bankAccount) throws SQLException, EntityNotFoundException {
        String query = String.format("SELECT * FROM credit " +
                        "WHERE bank_account_id = %d",
                bankAccount.getId());
        ResultSet table = statement.executeQuery(query);
        table.next();
        return convertTableToObject(table);
    }

    @Override
    public Credit save(Credit credit) throws SQLException, EntityNotFoundException {
        String query = String.format("INSERT INTO credit " +
                        "(credit_initialize_timestamp, bank_account_id, amount) " +
                        "VALUES (\"%s\", %d, %.2f)",
                credit.getCreditStartTime(),
                credit.getCreditedBankAccount().getId(),
                credit.getAmount());
        statement.execute(query);
        return findByBankAccount(credit.getCreditedBankAccount());
    }

    @Override
    public void update(Credit credit) throws SQLException {
        String query = String.format("UPDATE credit SET " +
                "credit_initialize_timestamp = \"%s\", " +
                "bank_account_id = %d, " +
                "amount = %.2f " +
                "WHERE id = %d",
                credit.getCreditStartTime(),
                credit.getCreditedBankAccount().getId(),
                credit.getAmount(),
                credit.getId());
        statement.executeUpdate(query);
    }

    @Override
    public Credit convertTableToObject(ResultSet table) throws SQLException {
        Credit credit = new Credit();
        credit.setId(table.getInt("id"));
        credit.setAmount(table.getDouble("amount"));
        credit.setCreditStartTime(table.getTimestamp("credit_initialize_timestamp"));

        BankAccountRepository bankAccountRepository = new BankAccountRepository(connection);
        credit.setCreditedBankAccount(bankAccountRepository
                .findById(table.getInt("bank_account_id")));

        return credit;
    }
}
