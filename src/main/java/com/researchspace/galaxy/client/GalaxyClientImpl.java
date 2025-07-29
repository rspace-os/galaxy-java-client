package com.researchspace.galaxy.client;

import com.researchspace.galaxy.model.input.upload.CreateDatasetCollectionOfPairsRequest;
import com.researchspace.galaxy.model.input.upload.CreateDatasetCollectionRequest;
import com.researchspace.galaxy.model.input.upload.PutAnnotationRequest;
import com.researchspace.galaxy.model.input.upload.UploadFileRequest;
import com.researchspace.galaxy.model.input.workflow.WorkflowInvocationRequest;
import com.researchspace.galaxy.model.output.history.History;
import com.researchspace.galaxy.model.output.upload.DataSet;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetAssociation;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetCollectionAssociation;
import com.researchspace.galaxy.model.output.upload.UploadFileResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationReport;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationStepStatusResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationSummaryStatusResponse;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class GalaxyClientImpl implements GalaxyClient {

  public static final int REQUEST_SIZE_LIMIT = 100;
  @Value("${galaxy.api.url}")
  private String galaxyApiUrl;
  private TusUploadHandler tusUploadHandler = new TusUploadHandler();
  private RestTemplate restTemplate = new RestTemplate();


  @Override
  public boolean testConnection(String apiKey)
      throws HttpServerErrorException, ResourceAccessException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    headers.add("upload-length", "1000");
    return restTemplate
        .exchange(
            galaxyApiUrl + "/upload/resumable_upload",
            HttpMethod.POST,
            new HttpEntity<>(null, headers),
            Object.class).getStatusCode() == HttpStatus.CREATED;
  }

  @Override
  public History createNewHistory(String apiKey, String historyName)
      throws HttpServerErrorException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("name", historyName);
    History response = restTemplate.exchange(
        galaxyApiUrl + "/histories",
        HttpMethod.POST,
        new HttpEntity<>(requestBody, headers),
        History.class).getBody();
    return response;
  }

  @SneakyThrows
  @Override
  public UploadFileResponse uploadFile(String historyID, String apiKey, File fileToUpload)
      throws HttpServerErrorException {
    String uploadSessionID = tusUploadHandler.uploadFile(new TusClient(),
        new TusUpload(fileToUpload), galaxyApiUrl
            + "/upload/resumable_upload", apiKey);
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    UploadFileRequest uploadFileRequest = new UploadFileRequest(historyID, uploadSessionID,
        fileToUpload.getName());
    UploadFileResponse response = restTemplate
        .exchange(
            galaxyApiUrl + "/tools/fetch",
            HttpMethod.POST,
            new HttpEntity<>(uploadFileRequest, headers),
            UploadFileResponse.class).getBody();
    log.info(String.format("Upload to Galaxy of file %s successful", fileToUpload.getName()));
    return response;
  }

  @SneakyThrows
  @Override
  public HistoryDatasetAssociation putAnnotationOnDataset(String historyID, String datasetId,
      String annotation, String apiKey)
      throws HttpServerErrorException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    PutAnnotationRequest putAnnotationRequest = new  PutAnnotationRequest(annotation);
    return restTemplate.exchange(
        galaxyApiUrl + "/histories/" + historyID + "/contents/datasets/"+datasetId,
        HttpMethod.PUT,
        new HttpEntity<>(putAnnotationRequest, headers),
        HistoryDatasetAssociation.class).getBody();
  }

  @Override
  public HistoryDatasetCollectionAssociation createDatasetCollectionOfPairs(String apiKey,
      String historyId, String collectionNameOfListOfPair, String pairName,
      String datasetIdForward, String datasetIdReverse) throws HttpServerErrorException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    CreateDatasetCollectionOfPairsRequest createDatasetCollectionOfPairsRequest = new CreateDatasetCollectionOfPairsRequest(
        collectionNameOfListOfPair, pairName,
        datasetIdForward, datasetIdReverse, historyId);
    HistoryDatasetCollectionAssociation response = restTemplate.exchange(
        galaxyApiUrl + "/dataset_collections",
        HttpMethod.POST,
        new HttpEntity<>(createDatasetCollectionOfPairsRequest, headers),
        HistoryDatasetCollectionAssociation.class).getBody();
    return response;
  }

  @Override
  public HistoryDatasetCollectionAssociation createDatasetCollection(String apiKey,
      String historyId, String collectionName, Map<String, String> dataFileNamesToIds)
      throws HttpServerErrorException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    CreateDatasetCollectionRequest createDatasetCollectionRequest = new CreateDatasetCollectionRequest(
        collectionName, dataFileNamesToIds, historyId);
    HistoryDatasetCollectionAssociation response = restTemplate.exchange(
        galaxyApiUrl + "/dataset_collections",
        HttpMethod.POST,
        new HttpEntity<>(createDatasetCollectionRequest, headers),
        HistoryDatasetCollectionAssociation.class).getBody();
    return response;
  }

  @Override
  public List<WorkflowInvocationResponse> invokeWorkflow(String apiKey,
      WorkflowInvocationRequest request, String workflowId) throws HttpServerErrorException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    List<WorkflowInvocationResponse> response = restTemplate.exchange(
        galaxyApiUrl + "/workflows/" + workflowId + "/invocations",
        HttpMethod.POST,
        new HttpEntity<>(request, headers),
        new ParameterizedTypeReference<List<WorkflowInvocationResponse>>() {
        }).getBody();
    return response;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<WorkflowInvocationResponse> getTopLevelInvocationsInAHistory(String apiKey,
      String historyId) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    List<WorkflowInvocationResponse> allResults = new java.util.ArrayList<>();
    ResponseEntity<List<WorkflowInvocationResponse>> response = null;
    for (int offset = 0; offset < Integer.MAX_VALUE; offset += REQUEST_SIZE_LIMIT) {
      response = restTemplate.exchange(
          galaxyApiUrl + "/invocations?include_nested_invocations=false&history_id="
              + historyId + "&limit=" + REQUEST_SIZE_LIMIT + "&offset=" + offset,
          HttpMethod.GET,
          new HttpEntity<>(null, headers),
          new ParameterizedTypeReference<List<WorkflowInvocationResponse>>() {
          });
      allResults.addAll(response.getBody());
      if (response.getBody().isEmpty() || response.getBody().size() < REQUEST_SIZE_LIMIT) {
        break;
      }
    }
    return allResults;
  }

  @Override
  public WorkflowInvocationSummaryStatusResponse getWorkflowInvocatioSummaryStatus(String apiKey,
      String invocationId) throws HttpServerErrorException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    WorkflowInvocationSummaryStatusResponse response = restTemplate.exchange(
        galaxyApiUrl + "/invocations/" + invocationId + "/jobs_summary",
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        WorkflowInvocationSummaryStatusResponse.class).getBody();
    return response;
  }

  @Override
  public WorkflowInvocationStepStatusResponse getWorkflowInvocationData(String apiKey,
      String invocationId) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    WorkflowInvocationStepStatusResponse response = restTemplate.exchange(
        galaxyApiUrl + "/invocations/" + invocationId,
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        WorkflowInvocationStepStatusResponse.class).getBody();
    return response;
  }

  @Override
  public WorkflowInvocationReport getWorkflowInvocationReport(String apiKey,
      String invocationId) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    WorkflowInvocationReport response = restTemplate.exchange(
        galaxyApiUrl + "/invocations/" + invocationId + "/report",
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        WorkflowInvocationReport.class).getBody();
    return response;
  }


  @Override
  public HistoryDatasetCollectionAssociation getDataSetCollectionDetails(String apiKey,
      String historyId, String dataSetId) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    HistoryDatasetCollectionAssociation response = restTemplate.exchange(
        galaxyApiUrl + "/histories/" + historyId + "/contents/dataset_collections/" + dataSetId,
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        HistoryDatasetCollectionAssociation.class).getBody();
    return response;
  }

  @Override
  public DataSet getDataSetDetails(String apiKey, String dataSetId) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("x-api-key", apiKey);
    DataSet response = restTemplate.exchange(
        galaxyApiUrl + "/datasets/" + dataSetId,
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        DataSet.class).getBody();
    return response;
  }
}


