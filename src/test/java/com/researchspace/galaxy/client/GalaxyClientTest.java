package com.researchspace.galaxy.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.researchspace.galaxy.model.output.upload.UploadFileResponse;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class GalaxyClientTest {

    public static final String TEST_FILE_TXT = "other_21May_sample_fasta.txt";
    private GalaxyClientImpl galaxyClient;
    private MockRestServiceServer mockServer;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TusUploadHandler mockTusUploadHandler;
    private File fileToUpload= new File("src/test/resources/files/other_21May_sample_fasta.txt");

    @BeforeEach
    public void setUp() throws Exception {
        initMocks(this);
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);

        // uploadFile has two parts, the first uses TusUploadHandler to do the actual transfer of
        //file data and the final part uses RestTemplate
        galaxyClient = new GalaxyClientImpl();
        ReflectionTestUtils.setField(galaxyClient, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(galaxyClient, "tusUploadHandler", mockTusUploadHandler);
    }

    @Test
    public void uploadFile_ShouldSucceed() throws Exception {
        // Given
        String historyId = "history123";
        String apiKey = "key123";
        String uploadSessionId = "session123";
        UploadFileResponse expectedResponse = new UploadFileResponse();

        when
                (mockTusUploadHandler.uploadFile(any(TusClient.class),any(TusUpload.class),
                eq("https://usegalaxy.eu/api/upload/resumable_upload"),
                eq(apiKey))).thenReturn(uploadSessionId);

        // Mock fetch endpoint
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
}