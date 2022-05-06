package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import general.Parameters;

public class DatabaseManager {
    protected Connection connection = null;
    private final String databaseFile;

    public DatabaseManager() {
        this.databaseFile = Parameters.databaseFile;
        this.connect();
    }

    /**
     * Stellt eine Verbindung zu der Datenbank her, die in der Parameter-Klasse
     * angegeben ist.
     */
    public void connect() {
        Properties properties = new Properties();
        properties.setProperty("PRAGMA foreign_keys", "ON");
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
            System.err.println("Error with database file " + databaseFile);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Database opened successfully!");
    }

    /**
     * Schliesst die Verbindung zu der Datenbank.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
