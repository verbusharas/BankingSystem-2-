package lt.verbus.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool {

    private static Connection connection = null;

    public static Connection getConnection (SqlDialect sqlDialect) throws SQLException, IOException{
        try (InputStream input = ConnectionPool.class.getClassLoader().getResourceAsStream("db.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
            }

            if (connection == null) {
                connection = DriverManager.getConnection(
                        properties.getProperty(sqlDialect.toString() + ".url"),
                        properties.getProperty(sqlDialect.toString() + ".username"),
                        properties.getProperty(sqlDialect.toString() + ".password")
                );
            }
            return connection;
        }
    }

    public static void closeConnections() throws SQLException {
        connection.close();
    }

}
