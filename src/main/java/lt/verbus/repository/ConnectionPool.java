package lt.verbus.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionPool {

    private static Connection connection;
    private static ConnectionPool instance;

    private ConnectionPool() throws SQLException, IOException {
        try (InputStream input = ConnectionPool.class.getClassLoader().getResourceAsStream("db.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
            }

            String sqlDialect = properties.getProperty("sqlDialect");

            connection = DriverManager.getConnection(
                    properties.getProperty(sqlDialect + ".url"),
                    properties.getProperty(sqlDialect + ".username"),
                    properties.getProperty(sqlDialect + ".password")
            );
        }
    }

    public static ConnectionPool getInstance() throws IOException, SQLException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void closeConnections() throws SQLException {
        connection.close();
    }

}
