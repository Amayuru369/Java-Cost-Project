package dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Shared database connection class.
 * All DAO classes use this to get a connection.
 *
 * IMPORTANT: Change PASSWORD to your local MySQL password before running.
 * Do NOT commit your password — add a config file to .gitignore if needed.
 *
 * Contributed by: Member 6 (Leader)
 */
public class DBConnection {
    private static final String URL      = "jdbc:mysql://localhost:3306/guesthouse_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "your_password"; // Change this locally

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (Exception e) {
                System.out.println("Connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
