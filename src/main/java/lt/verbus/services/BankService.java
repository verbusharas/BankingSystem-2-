package lt.verbus.services;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.Bank;
import lt.verbus.repository.BankRepository;

import java.sql.SQLException;
import java.util.List;

public class BankService {

    private final BankRepository bankRepository;

    public BankService (BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Bank> findAll() throws SQLException {
        return bankRepository.findAll();
    }

    public Bank findByBic(String bic) throws SQLException, EntityNotFoundException {
        return bankRepository.findByBic(bic);
    }

    public Bank findById(long id) throws SQLException {
        return bankRepository.findById(id);
    }

    public Bank save(Bank bank) throws SQLException, EntityNotFoundException {
        return bankRepository.save(bank);
    }

    public void update(Bank bank) throws SQLException {
        bankRepository.update(bank);
    }

    public void delete(Long id) throws SQLException {
        bankRepository.delete(id);
    }
}
