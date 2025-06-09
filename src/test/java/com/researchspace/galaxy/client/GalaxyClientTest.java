package com.researchspace.galaxy.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.researchspace.galaxy.model.input.workflow.SingleReadRNAFastQsWorkflowInvocationRequest;
import com.researchspace.galaxy.model.input.workflow.WorkflowInvocationRequest;
import com.researchspace.galaxy.model.output.history.History;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetCollectionAssociation;
import com.researchspace.galaxy.model.output.upload.UploadFileResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationStepStatusResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationSummaryStatusResponse;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class GalaxyClientTest {

  public static final String TEST_FILE_TXT = "other_21May_sample_fasta.txt";
  private GalaxyClient galaxyClient;
  private MockRestServiceServer mockServer;
  private RestTemplate restTemplate;
  private ObjectMapper objectMapper = new ObjectMapper();

  @Mock
  private TusUploadHandler mockTusUploadHandler;
  private File fileToUpload = new File("src/test/resources/files/other_21May_sample_fasta.txt");

  @BeforeEach
  public void setUp() throws Exception {
    initMocks(this);
    restTemplate = new RestTemplate();
    mockServer = MockRestServiceServer.createServer(restTemplate);
    galaxyClient = new GalaxyClientImpl();
    // uploadFile has two parts, the first uses TusUploadHandler to do the actual transfer of
    //file data and the final part uses RestTemplate
    ReflectionTestUtils.setField(galaxyClient, "restTemplate", restTemplate);
    ReflectionTestUtils.setField(galaxyClient, "tusUploadHandler", mockTusUploadHandler);
    ReflectionTestUtils.setField(galaxyClient, "galaxyApiUrl", "https://usegalaxy.eu/api");
  }

  @Test
  public void uploadFile_ShouldSucceed() throws Exception {
    // Given
    String historyId = "history123";
    String apiKey = "key123";
    String uploadSessionId = "session123";
    UploadFileResponse expectedResponse = new UploadFileResponse();

    when
        (mockTusUploadHandler.uploadFile(any(TusClient.class), any(TusUpload.class),
            eq("https://usegalaxy.eu/api/upload/resumable_upload"),
            eq(apiKey))).thenReturn(uploadSessionId);

    mockServer.expect(requestTo("https://usegalaxy.eu/api/tools/fetch"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(header("x-api-key", apiKey))
        .andExpect(jsonPath("$.history_id").value(historyId))
        .andExpect(jsonPath("$.files_0|file_data.session_id").value(uploadSessionId))
        .andExpect(jsonPath("$.targets[0].elements[0].name").value(TEST_FILE_TXT))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));

    UploadFileResponse result = galaxyClient.uploadFile(historyId, apiKey, fileToUpload);
    assertNotNull(result);
  }

  private MultiValueMap<String, String> getFormData(String key, String value) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add(key, value);
    return map;
  }

  @Test
  public void createHistoryShouldSucceed() throws Exception {
    String apiKey = "key123";
    History expectedResponse = new History();
    mockServer.expect(requestTo("https://usegalaxy.eu/api/histories"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(header("x-api-key", apiKey))
        .andExpect(content().formData(getFormData("name", "history123")))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));
    History response = galaxyClient.createNewHistory("key123", "history123");
    assertNotNull(response);
  }

  @Test
  public void testCreateDataSetCollectionSingleDataItemShouldSucceed() throws Exception {
    String apiKey = "key123";
    HistoryDatasetCollectionAssociation expectedResponse = new HistoryDatasetCollectionAssociation();
    mockServer.expect(requestTo("https://usegalaxy.eu/api/dataset_collections"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(header("x-api-key", apiKey))
        .andExpect(jsonPath("$.history_id").value("history123"))
        .andExpect(jsonPath("$.name").value("newCollectionName"))
        .andExpect(jsonPath("$.instance_type").value("history"))
        .andExpect(jsonPath("$.type").value("dataset_collection"))
        .andExpect(jsonPath("$.collection_type").value("list"))
        .andExpect(jsonPath("$.element_identifiers[0].name").value("datafileName"))
        .andExpect(jsonPath("$.element_identifiers[0].id").value("dataFileID"))
        .andExpect(jsonPath("$.element_identifiers[0].src").value("hda"))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));
    HistoryDatasetCollectionAssociation response = galaxyClient.createDatasetCollection("key123",
        "history123", "newCollectionName",
        Map.of("datafileName", "dataFileID"));
    assertNotNull(response);
  }
    @Test
    public void testCreateDataSetCollectionMultipleDataItemsShouldSucceed() throws Exception {
        String apiKey = "key123";
        HistoryDatasetCollectionAssociation expectedResponse = new HistoryDatasetCollectionAssociation();
        mockServer.expect(requestTo("https://usegalaxy.eu/api/dataset_collections"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(header("x-api-key", apiKey))
            .andExpect(jsonPath("$.history_id").value("history123"))
            .andExpect(jsonPath("$.name").value("newCollectionName"))
            .andExpect(jsonPath("$.instance_type").value("history"))
            .andExpect(jsonPath("$.type").value("dataset_collection"))
            .andExpect(jsonPath("$.collection_type").value("list"))
            .andExpect(jsonPath("$.element_identifiers[0].name").value("datafileName1"))
            .andExpect(jsonPath("$.element_identifiers[0].id").value("dataFileID1"))
            .andExpect(jsonPath("$.element_identifiers[0].src").value("hda"))
            .andExpect(jsonPath("$.element_identifiers[1].name").value("datafileName2"))
            .andExpect(jsonPath("$.element_identifiers[1].id").value("dataFileID2"))
            .andExpect(jsonPath("$.element_identifiers[1].src").value("hda"))
            .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(expectedResponse)));
        LinkedHashMap<String, String> dataFileNames = new LinkedHashMap<>();
        dataFileNames.put("datafileName1", "dataFileID1");
        dataFileNames.put("datafileName2", "dataFileID2");
        HistoryDatasetCollectionAssociation response = galaxyClient.createDatasetCollection("key123",
            "history123", "newCollectionName",
            dataFileNames);
        assertNotNull(response);
    }

  @Test
  public void testCreateDataSetCollectiionPairShouldSucceed() throws Exception {
    String apiKey = "key123";
    HistoryDatasetCollectionAssociation expectedResponse = new HistoryDatasetCollectionAssociation();
    mockServer.expect(requestTo("https://usegalaxy.eu/api/dataset_collections"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(header("x-api-key", apiKey))
        .andExpect(jsonPath("$.history_id").value("history123"))
        .andExpect(jsonPath("$.name").value("newCollectionPairName"))
        .andExpect(jsonPath("$.instance_type").value("history"))
        .andExpect(jsonPath("$.type").value("dataset_collection"))
        .andExpect(jsonPath("$.collection_type").value("list:paired"))
        .andExpect(jsonPath("$.element_identifiers[0].name").value("theNewPairName"))
        .andExpect(jsonPath("$.element_identifiers[0].src").value("new_collection"))
        .andExpect(
            jsonPath("$.element_identifiers[0].element_identifiers[0].name").value("forward"))
        .andExpect(
            jsonPath("$.element_identifiers[0].element_identifiers[0].id").value("idForward"))
        .andExpect(jsonPath("$.element_identifiers[0].element_identifiers[0].src").value("hda"))
        .andExpect(
            jsonPath("$.element_identifiers[0].element_identifiers[1].name").value("reverse"))
        .andExpect(
            jsonPath("$.element_identifiers[0].element_identifiers[1].id").value("idReverse"))
        .andExpect(jsonPath("$.element_identifiers[0].element_identifiers[1].src").value("hda"))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));
    HistoryDatasetCollectionAssociation response = galaxyClient.createDatasetCollectionOfPairs(
        "key123", "history123", "newCollectionPairName", "theNewPairName",
        "idForward", "idReverse");
    assertNotNull(response);
  }

  @Test
  public void testConnectionShouldSucceed() throws Exception {
    String apiKey = "key123";
    mockServer.expect(requestTo("https://usegalaxy.eu/api/upload/resumable_upload"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(header("x-api-key", apiKey))
        .andRespond(withStatus(HttpStatus.CREATED));
    assertTrue(galaxyClient.testConnection("key123"));
  }

  @Test
  public void invokeWorkFlowShouldSucceed() throws JsonProcessingException {
    String apiKey = "key123";
    String workflowId = "workflowId";
    String historyId = "historyId";
    String datasetId = "datasetId";
    List<WorkflowInvocationResponse> expectedResponse = List.of(new WorkflowInvocationResponse());
    WorkflowInvocationRequest request = new SingleReadRNAFastQsWorkflowInvocationRequest(historyId,
        datasetId);
    mockServer.expect(
            requestTo("https://usegalaxy.eu/api/workflows/" + workflowId + "/invocations"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(header("x-api-key", apiKey))
        .andExpect(jsonPath("$.history_id").value(historyId))
        .andExpect(jsonPath("$.inputs.0.values[0].id").value(datasetId))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));
    List<WorkflowInvocationResponse> response = galaxyClient.invokeWorkflow("key123", request,
        workflowId);
    assertNotNull(response.get(0));
  }

  @Test
  public void testGetTopLevelInvocationsInAHistoryWhen100ExistShouldSucceed() throws Exception {
    String apiKey = "key123";
    String historyId = "history123";
    List<WorkflowInvocationResponse> expectedResponse = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      expectedResponse.add(new WorkflowInvocationResponse());
    }
    mockServer.expect(requestTo(
            "https://usegalaxy.eu/api/invocations?include_nested_invocations=false&history_id="
                + historyId
                + "&limit=" + 100 + "&offset=" + 0))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("x-api-key", apiKey))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));
    mockServer.expect(requestTo(
            "https://usegalaxy.eu/api/invocations?include_nested_invocations=false&history_id="
                + historyId
                + "&limit=" + 100 + "&offset=" + 100))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("x-api-key", apiKey))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(new ArrayList<>())));
    List<WorkflowInvocationResponse> response = galaxyClient.getTopLevelInvocationsInAHistory(
        apiKey, historyId);
    mockServer.verify();
    assertNotNull(response.get(0));
  }

  @Test
  public void testGetTopLevelInvocationsInAHistoryWhen99ExistShouldSucceed() throws Exception {
    String apiKey = "key123";
    String historyId = "history123";
    List<WorkflowInvocationResponse> expectedResponse = new ArrayList<>();
    for (int i = 0; i < 99; i++) {
      expectedResponse.add(new WorkflowInvocationResponse());
    }
    mockServer.expect(ExpectedCount.times(1), requestTo(
            "https://usegalaxy.eu/api/invocations?include_nested_invocations=false&history_id="
                + historyId
                + "&limit=" + 100 + "&offset=" + 0))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("x-api-key", apiKey))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));
    List<WorkflowInvocationResponse> response = galaxyClient.getTopLevelInvocationsInAHistory(
        apiKey, historyId);
    mockServer.verify();
    assertNotNull(response.get(0));
  }

  @Test
  public void testGetWorkflowInvocationSummaryStatusShouldSucceed() throws Exception {
    String apiKey = "key123";
    String invocationId = "invocation123";
    WorkflowInvocationSummaryStatusResponse expectedResponse = new WorkflowInvocationSummaryStatusResponse();
    mockServer.expect(
            requestTo("https://usegalaxy.eu/api/invocations/" + invocationId + "/jobs_summary"))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("x-api-key", apiKey))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));
    WorkflowInvocationSummaryStatusResponse response = galaxyClient.getWorkflowInvocatioSummaryStatus(
        apiKey, invocationId);
    assertNotNull(response);
  }

  @Test
  public void testGetWorkflowInvocationDataShouldSucceed() throws Exception {
    String apiKey = "key123";
    String invocationId = "invocation123";
    WorkflowInvocationStepStatusResponse expectedResponse = new WorkflowInvocationStepStatusResponse();
    mockServer.expect(requestTo("https://usegalaxy.eu/api/invocations/" + invocationId))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("x-api-key", apiKey))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(expectedResponse)));
    WorkflowInvocationStepStatusResponse response = galaxyClient.getWorkflowInvocationData(apiKey,
        invocationId);
    assertNotNull(response);
  }
}