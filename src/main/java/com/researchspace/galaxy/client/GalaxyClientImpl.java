package com.researchspace.galaxy.client;

import com.researchspace.galaxy.model.input.upload.UploadFileRequest;
import com.researchspace.galaxy.model.output.history.History;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetCollectionAssociation;
import com.researchspace.galaxy.model.output.upload.UploadFileResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationStepStatusResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationSummaryStatusResponse;
import com.researchspace.galaxy.utils.FileChunkSizeCalculator;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
@Slf4j
@Component
public class GalaxyClientImpl implements GalaxyClient {
    //TODO define a property to configure this URL
    private static final String GALAXY_URL = "https://usegalaxy.eu/";
    private TusUploadHandler tusUploadHandler  = new TusUploadHandler();
    private RestTemplate restTemplate = new RestTemplate();
    //The standard JDK HTTP library does not support PATCH requests, so we need to use the Apache HTTP client
    // see https://stackoverflow.com/questions/29447382/resttemplate-patch-request
    //TODO make sure the org.apache.httpcomponents dependency is excluded from RSpace Pom as it has its own version
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    @SneakyThrows
    public GalaxyClientImpl() {
        restTemplate.setRequestFactory(requestFactory);
        //see https://medium.com/red6-es/uploading-a-file-with-a-filename-with-spring-resttemplate-8ec5e7dc52ca
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(converter);
    }

    private FileChunkSizeCalculator fileChunkSizeCalculator = new FileChunkSizeCalculator();

    @Override
    public boolean testConnection(String apiKey) throws HttpServerErrorException, ResourceAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);
        headers.add("upload-length", "1000");
        return restTemplate
                .exchange(
                        GALAXY_URL+"api/upload/resumable_upload",
                        HttpMethod.POST,
                        new HttpEntity<>(null, headers),
                        Object.class).getStatusCode() == HttpStatus.CREATED;
    }

    @Override
    public History createNewHistory(String apiKey, String historyName) throws HttpServerErrorException {
        return null;
    }

    @SneakyThrows
    @Override
    public UploadFileResponse uploadFile(String historyID, String apiKey, File fileToUpload) throws HttpServerErrorException {
        String uploadSessionID =  tusUploadHandler.uploadFile(new TusClient(), new TusUpload(fileToUpload), GALAXY_URL+"api/upload/resumable_upload", apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(historyID,uploadSessionID, fileToUpload.getName());
        UploadFileResponse response = restTemplate
                .exchange(
                        GALAXY_URL+"api/tools/fetch",
                        HttpMethod.POST,
                        new HttpEntity<>(uploadFileRequest, headers),
                        UploadFileResponse.class).getBody();
        log.info(String.format("Upload to Galaxy of file %s successful", fileToUpload.getName()));
        return response;
    }

//    ResponseEntity<Object> uploadFileSize(String apiKey, File fieToUpload) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("x-api-key", apiKey);
//        headers.add("upload-length", fileChunkSizeCalculator.calculateFileChunkSize(fieToUpload)+"");
//        return  restTemplate
//                .exchange(
//                        GALAXY_URL+"api/upload/resumable_upload",
//                        HttpMethod.POST,
//                        new HttpEntity<>(null, headers),
//                        Object.class);
//    }
//
//    ResponseEntity<Object> uploadFileContents(String apiKey, File fileToUpload, long offset, String location) throws IOException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("x-api-key", apiKey);
//        headers.add("upload-offest", ""+offset);
//        headers.add("content-length", ""+
//                fileChunkSizeCalculator.calculateLengthWithOffset(fileToUpload,offset));
////        headers.add("content-type", "application/offset+octet-stream");
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fileToUpload.toPath()));
//        MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
//        data.add("file", resource);
//        ResponseEntity<Object> response = null;
//        if(offset<fileToUpload.length()) {
//            response = restTemplate
//                    .exchange(
//                            location,
//                            HttpMethod.PATCH,
//                            new HttpEntity<>(data, headers),
//                            Object.class);
//        }
//        response.getHeaders().get("Upload-Offset").forEach(System.out::println);
//        return response;
//    }

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
