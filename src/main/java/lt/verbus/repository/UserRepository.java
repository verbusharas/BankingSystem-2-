package lt.verbus.repository;

import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.User;

import java.sql.*;
import java.util.List;

public class UserRepository extends GenericRepository<User> {

    public UserRepository(Connection connection) throws SQLException {
        super(connection, "user");
    }

    public User findByUsername(String username) throws SQLException, EntityNotFoundException {
        return super.findByUniqueCode("username", username);
    }

    @Override
    public User save(User user) throws SQLException, EntityNotFoundException {
        String query = String.format("INSERT INTO user " +
                        "(username, full_name, phone_number) " +
                        "VALUES (\"%s\", \"%s\", \"%s\")",
                user.getUsername(), user.getFullName(), user.getPhoneNumber());
        statement.execute(query);
        return findByUsername(user.getUsername());
    }

    @Override
    public void update(User user) throws SQLException {
        String query = String.format("UPDATE user SET " +
                        "username = \"%s\", full_name = \"%s\", phone_number = \"%s\" " +
                        "WHERE id = %d",
                user.getUsername(), user.getFullName(), user.getPhoneNumber(),
                user.getId());
        statement.executeUpdate(query);
    }

    @Override
    protected User convertTableToObject(ResultSet table) throws SQLException {
        User user = new User();
        user.setId(table.getInt("id"));
        user.setUsername(table.getString("username"));
        user.setFullName(table.getString("full_name"));
        user.setPhoneNumber(table.getString("phone_number"));
        return user;
    }
}
