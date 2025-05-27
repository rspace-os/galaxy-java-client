package com.researchspace.galaxy.client;

import com.researchspace.galaxy.model.output.history.History;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetAssociation;
import com.researchspace.galaxy.model.output.upload.UploadFileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        ReflectionTestUtils.setField(client, "galaxyUrl", "https://usegalaxy.eu/");
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
        //TODO - create a history 1st and use that HistoryID for this test
        UploadFileResponse response =  client.uploadFile("37dba3081eef0d8d", API_KEY, fileToUpload);
        List<HistoryDatasetAssociation> outputs =  response.getOutputs();
        assertTrue(outputs.get(0).getName().equals("other_21May_sample_fasta.txt"));
    }

    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testCreateNewHistory() {
        History response = client.createNewHistory(API_KEY, "testCreateNewHistory");
        assertTrue(response.getName().equals("testCreateNewHistory"));
    }
}