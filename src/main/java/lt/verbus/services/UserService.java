package lt.verbus.services;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.User;
import lt.verbus.repository.UserRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService() throws IOException, SQLException {
        this.userRepository = new UserRepository();
    }

    public List<User> findAll() throws SQLException, IOException {
        return userRepository.findAll();
    }

    public User findByUsername(String username) throws SQLException, EntityNotFoundException, IOException {
        return userRepository.findByUsername(username);
    }

    public User findById(long id) throws SQLException, IOException {
        return userRepository.findById(id);
    }

    public User save(User user) throws SQLException, EntityNotFoundException, IOException {
        return userRepository.save(user);
    }

    public void update(User user) throws SQLException {
        userRepository.update(user);
    }

    public void delete(Long id) throws SQLException {
        userRepository.delete(id);
    }


}
