package com.researchspace.galaxy.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.File;

/**
 * Runs if two System properties are correct: "nightly" is set to "true" and API_KEY is set with a valid value for the Galaxy instance.
 */
public class GalaxyClientRealConnectionTest {
    public static final String API_KEY = System.getProperty("API_KEY");

    private GalaxyClient client;
    private File fileToUpload;

    @BeforeEach
    public void setUp() {
        client = new GalaxyClientImpl();
        fileToUpload = new File("src/test/resources/files/other_21May_sample_fasta.txt");
    }

    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testGetConnectionToGalaxyReturnsCreatedResponse() {
        client.testConnection(API_KEY);
    }

    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testUploadFile() {
        client.uploadFile("37dba3081eef0d8d", API_KEY, fileToUpload);
    }
}