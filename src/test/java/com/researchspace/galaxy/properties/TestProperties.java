package com.researchspace.galaxy.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestProperties {
    private static String propertyFileName =  System.getProperty("TEST_PROPERTIES");
    private static Properties properties;

    public static Properties getProperties() {
        if (propertyFileName == null) {
            propertyFileName = "rspace-test.properties";
        }
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = TestProperties.class
                    .getClassLoader()
                    .getResourceAsStream(propertyFileName)) {
                if (input == null) {
                    throw new IllegalStateException("Cannot find properties file: "+ propertyFileName);
                }
                properties.load(input);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load properties", e);
            }
        }
        return properties;
    }

    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }
}

