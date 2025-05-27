package com.researchspace.galaxy.client;

import com.researchspace.galaxy.model.input.upload.UploadFileRequest;
import com.researchspace.galaxy.model.output.history.History;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetCollectionAssociation;
import com.researchspace.galaxy.model.output.upload.UploadFileResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationStepStatusResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationSummaryStatusResponse;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
@Slf4j
@Component
public class GalaxyClientImpl implements GalaxyClient {
    @Value("${galaxy.api.url}")
    private String galaxyUrl;
    private TusUploadHandler tusUploadHandler  = new TusUploadHandler();
    private RestTemplate restTemplate = new RestTemplate();


    @Override
    public boolean testConnection(String apiKey) throws HttpServerErrorException, ResourceAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);
        headers.add("upload-length", "1000");
        return restTemplate
                .exchange(
                        galaxyUrl +"api/upload/resumable_upload",
                        HttpMethod.POST,
                        new HttpEntity<>(null, headers),
                        Object.class).getStatusCode() == HttpStatus.CREATED;
    }

    @Override
    public History createNewHistory(String apiKey, String historyName) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("name",historyName);
        History response = restTemplate.exchange(
                galaxyUrl +"api/histories",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                History.class).getBody();
        return response;
    }

    @SneakyThrows
    @Override
    public UploadFileResponse uploadFile(String historyID, String apiKey, File fileToUpload) throws HttpServerErrorException {
        String uploadSessionID =  tusUploadHandler.uploadFile(new TusClient(), new TusUpload(fileToUpload), galaxyUrl +"api/upload/resumable_upload", apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(historyID,uploadSessionID, fileToUpload.getName());
        UploadFileResponse response = restTemplate
                .exchange(
                        galaxyUrl +"api/tools/fetch",
                        HttpMethod.POST,
                        new HttpEntity<>(uploadFileRequest, headers),
                        UploadFileResponse.class).getBody();
        log.info(String.format("Upload to Galaxy of file %s successful", fileToUpload.getName()));
        return response;
    }

    @Override
    public HistoryDatasetCollectionAssociation createDatasetCollectionOfPairs(String apiKey, String historyId, String nameListOfPair, String collectionName, String datasetIdForward, String DataserIdReverse) throws HttpServerErrorException {
        return null;
    }

    @Override
    public WorkflowInvocationResponse invokeWorkflow(String apiKey, String historyId, String workflowId, String datasetId) throws HttpServerErrorException {
        return null;
    }

    @Override
    public WorkflowInvocationSummaryStatusResponse getWorkflowInvocationOverallStatus(String apiKey, String invocationId) throws HttpServerErrorException {
        return null;
    }

    @Override
    public WorkflowInvocationStepStatusResponse getWorkflowStepsInvocationStatus(String apiKey, String invocationId) throws HttpServerErrorException {
        return null;
    }

    @Override
    public String getHistoryLink(String apiKey, String invocationId) throws HttpServerErrorException, IOException {
        return "";
    }
}
