package com.researchspace.galaxy.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.researchspace.galaxy.model.input.workflow.PairedEndRNAFastQsWorkflowInvocationRequest;
import com.researchspace.galaxy.model.input.workflow.SingleReadRNAFastQsWorkflowInvocationRequest;
import com.researchspace.galaxy.model.output.history.History;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetAssociation;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetCollectionAssociation;
import com.researchspace.galaxy.model.output.upload.UploadFileResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationStepStatusResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationSummaryStatusResponse;
import com.researchspace.galaxy.properties.TestProperties;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Runs if two System properties are correct: "nightly" is set to "true" and GALAXY_API_KEY is set with a valid value for the Galaxy instance.
 */
public class GalaxyClientRealConnectionTest {
    public static final String GALAXY_API_KEY = System.getProperty("GALAXY_API_KEY");
    public static final String TEST_CREATE_NEW_HISTORY = "testCreateNewHistory";
    public static final String TEST_CREATE_NEW_SINGLE_DATASET = "testCreateNewSingleDataset";
    public static final String FILE_TO_UPLOAD_PATH="src/test/resources/files/other_21May_sample_fasta.txt";
    public static final String FILE_TO_UPLOAD_REVERSE_PAIR_PATH="src/test/resources/files/sample_fasta_data.txt";
    private static final String TEST_CREATE_NEW_PAIRED_LIST_DATASET = "testCreateNewPairedListDataset";
    private static final String TEST_CREATE_NEW_PAIRED_PAIRNAME = "testNewPair";
    // There needs to be existing data on a Galaxy instance in order to invoke workflows, this cannot be done 'on the fly' in a test
    private static final String TEST_EXISTING_PAIRED_END_RNA_WORKFLOW_ID = TestProperties.getProperty("testExistingPairedEndRNAFastQsWorkflowId");
    private static final String TEST_EXISTING_SINGLE_END_RNA_WORKFLOW_ID = TestProperties.getProperty("testExistingSingleReadRNAFastQsWorkflowId");
    private static  final String TEST_EXISTING_HISTORY_ID = TestProperties.getProperty("testExistingHistoryId");
    private static  final String TEST_EXISTING_PAIRED_RNA_DATASET_ID = TestProperties.getProperty("testExistingPairedRnaDatasetId");
    private static  final String TEST_EXISTING_SINGLE_RNA_DATASET_ID = TestProperties.getProperty("testExistingSingleRnaDatasetId");
    private static  final String TEST_EXISTING_INVOCATION_ID = TestProperties.getProperty("testExistingInvocationId");
    private static final String GALAXY_URL = TestProperties.getProperty("galaxyUrl");
    private ObjectMapper objectMapper = new ObjectMapper();
    private GalaxyClient client;
    private File fileToUpload;
    private File reversePairFileToUpload ;
    @BeforeEach
    public void setUp() {
        client = new GalaxyClientImpl();
        ReflectionTestUtils.setField(client, "galaxyApiUrl", GALAXY_URL);
        fileToUpload = new File(FILE_TO_UPLOAD_PATH);
        reversePairFileToUpload = new File(FILE_TO_UPLOAD_REVERSE_PAIR_PATH);
    }

    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testGetConnectionToGalaxyReturnsCreatedResponse() {
        client.testConnection(GALAXY_API_KEY);
    }

    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testCreateNewHistory() {
        History newlyCreatedHistory = createNewHistory();
        assertTrue(newlyCreatedHistory.getName().equals("testCreateNewHistory"));
    }

    private History createNewHistory() {
        return client.createNewHistory(GALAXY_API_KEY, TEST_CREATE_NEW_HISTORY);
    }

    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testUploadFile() {
        HistoryDatasetAssociation newlyUploadedFile = createNewlyUploadedFileInNewHistory();
        assertTrue(newlyUploadedFile.getName().equals("other_21May_sample_fasta.txt"));
    }

    private HistoryDatasetAssociation createNewlyUploadedFileInNewHistory(File toUse) {
        History newlyCreatedHistory = createNewHistory();
        UploadFileResponse response =  client.uploadFile(newlyCreatedHistory.getId(), GALAXY_API_KEY, toUse);
        List<HistoryDatasetAssociation> outputs =  response.getOutputs();
        return outputs.get(0);
    }

    private HistoryDatasetAssociation createNewlyUploadedFileInNewHistory() {
       return createNewlyUploadedFileInNewHistory(fileToUpload);
    }

    private HistoryDatasetAssociation createNewlyUploadedFileInExistingHistory(String historyId, File toUse) {
        UploadFileResponse response =  client.uploadFile(historyId, GALAXY_API_KEY, toUse);
        List<HistoryDatasetAssociation> outputs =  response.getOutputs();
        return outputs.get(0);
    }

    private HistoryDatasetAssociation createNewlyUploadedFileInExistingHistory(String historyId) {
        return createNewlyUploadedFileInExistingHistory(historyId, fileToUpload);
    }

    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testCreateDatasetCollection() {
        HistoryDatasetAssociation newlyUploadedFile = createNewlyUploadedFileInNewHistory();
        HistoryDatasetCollectionAssociation newlyCreatedDataset = client.createDatasetCollection(GALAXY_API_KEY, newlyUploadedFile.getHistoryId(),TEST_CREATE_NEW_SINGLE_DATASET,
                fileToUpload.getName(),newlyUploadedFile.getDatasetId());
        assertEquals(TEST_CREATE_NEW_SINGLE_DATASET, newlyCreatedDataset.getName());
    }

    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testCreateDatasetCollectionPair() {
        HistoryDatasetAssociation newlyUploadedFileForward = createNewlyUploadedFileInNewHistory();
        HistoryDatasetAssociation newlyUploadedFileReverse = createNewlyUploadedFileInExistingHistory(newlyUploadedFileForward.getHistoryId(), reversePairFileToUpload);
        HistoryDatasetCollectionAssociation newlyCreatedDataset = client.createDatasetCollectionOfPairs(GALAXY_API_KEY, newlyUploadedFileForward.getHistoryId(),TEST_CREATE_NEW_PAIRED_LIST_DATASET,
                TEST_CREATE_NEW_PAIRED_PAIRNAME, newlyUploadedFileForward.getDatasetId(),newlyUploadedFileReverse.getDatasetId());
        assertEquals(TEST_CREATE_NEW_PAIRED_LIST_DATASET, newlyCreatedDataset.getName());
    }

    @Test
    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    public void testInvokePairedEndRnaWorkflow() throws JsonProcessingException {
        PairedEndRNAFastQsWorkflowInvocationRequest request = new PairedEndRNAFastQsWorkflowInvocationRequest(TEST_EXISTING_HISTORY_ID,TEST_EXISTING_PAIRED_RNA_DATASET_ID);
        System.out.println(objectMapper.writeValueAsString(request));
        List<WorkflowInvocationResponse> response = client.invokeWorkflow(GALAXY_API_KEY,request,TEST_EXISTING_PAIRED_END_RNA_WORKFLOW_ID);
        assertNotNull(response.get(0).getInvocationId());
        // Steps to see the running workflow invoked by this test in Galaxy GUI:
        // Switch to the history matching TEST_EXISTING_HISTORY_ID. On the right hand side of GUI choose menu item with 3 horizontal bars, then choose 'show invocations'. Select the newest invocation.
    }

    @Test
    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    public void testInvokeSingleEndRnaWorkflow() throws JsonProcessingException {
        SingleReadRNAFastQsWorkflowInvocationRequest request = new SingleReadRNAFastQsWorkflowInvocationRequest(TEST_EXISTING_HISTORY_ID,TEST_EXISTING_SINGLE_RNA_DATASET_ID);
        System.out.println(objectMapper.writeValueAsString(request));
        List<WorkflowInvocationResponse> response = client.invokeWorkflow(GALAXY_API_KEY,request,TEST_EXISTING_SINGLE_END_RNA_WORKFLOW_ID);
        assertNotNull(response.get(0).getInvocationId());
        // Steps to see the running workflow invoked by this test in Galaxy GUI:
        // Switch to the history matching TEST_EXISTING_HISTORY_ID. On the right hand side of GUI choose menu item with 3 horizontal bars, then choose 'show invocations'. Select the newest invocation.
    }

    /**
     * Requires there to be an existing invocation on a Galaxy instance with ID = TEST_EXISTING_INVOCATION_ID
     */
    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    @Test
    public void testGetWorkflowInvocationSummaryStatus() {
        WorkflowInvocationSummaryStatusResponse response = client.getWorkflowInvocatioSummaryStatus(GALAXY_API_KEY,TEST_EXISTING_INVOCATION_ID);
        assertTrue(response.getPopulatedState().equalsIgnoreCase("ok"));//dont know if case is guaranteed?
    }

    /**
     * Requires there to be an existing invocation on a Galaxy instance with ID = TEST_EXISTING_INVOCATION_ID
     */
    @Test
    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    public void testGetInvocationsForHistory() {
       List<WorkflowInvocationResponse> responses =  client.getTopLevelInvocationsInAHistory(GALAXY_API_KEY,TEST_EXISTING_HISTORY_ID);
       assertTrue(responses.stream().anyMatch(response -> response.getInvocationId().equals(TEST_EXISTING_INVOCATION_ID)));
    }

    /**
     * Requires there to be an existing invocation on a Galaxy instance with ID = TEST_EXISTING_INVOCATION_ID
     */
    @Test
    @EnabledIfSystemProperty(named = "nightly", matches = "true")
    public void testGetWorkflowInvocationData() {
        WorkflowInvocationStepStatusResponse response =  client.getWorkflowInvocationData(GALAXY_API_KEY,TEST_EXISTING_INVOCATION_ID);
        assertEquals(response.getInvocationId(), TEST_EXISTING_INVOCATION_ID);
    }

}