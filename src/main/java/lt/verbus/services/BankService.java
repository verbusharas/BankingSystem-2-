package lt.verbus.services;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.Bank;
import lt.verbus.repository.BankRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BankService {

    private final BankRepository bankRepository;

    public BankService () throws IOException, SQLException {
        bankRepository = new BankRepository();
    }

    public List<Bank> findAll() throws SQLException, IOException {
        return bankRepository.findAll();
    }

    public Bank findByBic(String bic) throws SQLException, EntityNotFoundException, IOException {
        return bankRepository.findByBic(bic);
    }

    public Bank findById(long id) throws SQLException, IOException {
        return bankRepository.findById(id);
    }

    public Bank save(Bank bank) throws SQLException, EntityNotFoundException, IOException {
        return bankRepository.save(bank);
    }

    public void update(Bank bank) throws SQLException {
        bankRepository.update(bank);
    }

    public void delete(Long id) throws SQLException {
        bankRepository.delete(id);
    }
}
