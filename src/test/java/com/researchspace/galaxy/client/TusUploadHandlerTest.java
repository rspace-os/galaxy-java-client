package com.researchspace.galaxy.client;

import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TusUploadHandlerTest {
    public static final int DEFAULT_CHUNK_SIZE = 10485760;
    @Mock
    private TusClient tusClientMock;
    @Mock
    private TusUpload tusUploadMock;
    @Mock
    private TusUploader tusUploaderMock;
    private TusUploadHandler testee;
    private String apiKey = "apiKey";
    private String sessionID = "sessionID";
    private Map<String, String> headers = Map.ofEntries(
            Map.entry("x-api-key", apiKey));

    @BeforeEach
    public void setUp() {
        initMocks(this);
        testee = new TusUploadHandler();
    }

    @SneakyThrows
    @Test
    public void uploadCallsClient() {
        URL uploadPath = new URL("https://uploadPath");
        when(tusClientMock.resumeOrCreateUpload(eq(tusUploadMock))).thenReturn(tusUploaderMock);
        when(tusUploaderMock.uploadChunk()).thenReturn(-1);
        when(tusUploaderMock.getUploadURL()).thenReturn(new URL(uploadPath + "/" + sessionID));
        String sessionIDResponse = testee.uploadFile(tusClientMock, tusUploadMock, "https://uploadPath", apiKey);
        verify(tusClientMock).setUploadCreationURL(eq(uploadPath));
        assertTrue(sessionIDResponse.equals(sessionID));
        verify(tusUploaderMock).setChunkSize(eq(DEFAULT_CHUNK_SIZE));
        verify(tusUploaderMock).finish();
        verify(tusClientMock).setHeaders(eq(headers));
    }
}
