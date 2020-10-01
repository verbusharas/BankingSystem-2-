package lt.verbus;

import lt.verbus.controller.Controller;
import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.repository.ConnectionPool;

import java.io.IOException;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws IOException, SQLException, EntityNotFoundException {
        new Controller().launchApp();
        ConnectionPool.closeConnections();
    }
}
