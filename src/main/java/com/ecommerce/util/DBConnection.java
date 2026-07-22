package com.ecommerce.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton DB connection utility.
 * Loads configuration from db.properties on the classpath.
 */
public class DBConnection {

    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("db.properties not found in classpath");
            }
            Properties props = new Properties();
            props.load(input);
            driver   = props.getProperty("db.driver");
            url      = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            Class.forName(driver);
            logger.info("Database driver loaded: {}", driver);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Failed to initialize DBConnection", e);
            throw new RuntimeException("DB initialization failed", e);
        }
    }

    private DBConnection() {}

    /**
     * Returns a new database connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Safely closes a connection.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("Failed to close connection", e);
            }
        }
    }
}
