package lt.verbus.repository;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.Bank;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BankRepository extends GenericRepository<Bank> {

    public BankRepository() throws SQLException, IOException {
        super("bank");
    }

    public Bank findByBic(String bic) throws SQLException, EntityNotFoundException, IOException {
        return super.findByUniqueCode("bic", bic);
    }

    @Override
    public Bank save(Bank bank) throws SQLException, EntityNotFoundException, IOException {
        String query = String.format("INSERT INTO bank " +
                        "(name, bic) " +
                        "VALUES (\"%s\", \"%s\")",
                bank.getName(), bank.getBic());
        statement.execute(query);
        return findByBic(bank.getBic());
    }

    @Override
    public void update(Bank bank) throws SQLException {
        String query = String.format("UPDATE bank SET " +
                        "name = \"%s\", bic = \"%s\" WHERE id = %d",
                bank.getName(), bank.getBic(), bank.getId());
        statement.executeUpdate(query);
    }

    @Override
    protected Bank convertTableToObject(ResultSet table) throws SQLException {
        Bank bank = new Bank();
        bank.setId(table.getInt("id"));
        bank.setBic(table.getString("bic"));
        bank.setName(table.getString("name"));
        return bank;
    }
}
