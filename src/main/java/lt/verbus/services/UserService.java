package lt.verbus.services;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.User;
import lt.verbus.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() throws SQLException {
        return userRepository.findAll();
    }

    public User findByUsername(String username) throws SQLException, EntityNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User findById(long id) throws SQLException {
        return userRepository.findById(id);
    }

    public User save(User user) throws SQLException, EntityNotFoundException {
        return userRepository.save(user);
    }

    public void update(User user) throws SQLException {
        userRepository.update(user);
    }

    public void delete(Long id) throws SQLException {
        userRepository.delete(id);
    }


}
