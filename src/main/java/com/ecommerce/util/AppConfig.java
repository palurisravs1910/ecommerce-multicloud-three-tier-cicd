package com.ecommerce.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads application-wide configuration from db.properties.
 */
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final Properties props = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("db.properties not found");
            }
            props.load(input);
        } catch (IOException e) {
            logger.error("Failed to load AppConfig", e);
            throw new RuntimeException("AppConfig load failed", e);
        }
    }

    private AppConfig() {}

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String getStripeSecretKey() {
        return props.getProperty("stripe.secret.key");
    }

    public static String getStripePublishableKey() {
        return props.getProperty("stripe.publishable.key");
    }
}
