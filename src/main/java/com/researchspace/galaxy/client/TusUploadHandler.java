package com.researchspace.galaxy.client;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusExecutor;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
@Slf4j
public class TusUploadHandler {
    public static final int DEFAULT_CHUNK_SIZE = 10485760;

    /*
     * Upload a file to Galaxy using the TUS protocol.
     * Returns the sessionID of the upload which will be used to initiate the processing of the file by Galaxy
     */
    public String uploadFile(TusClient tusClient,TusUpload tusUpload, String uploadPath, String apiKey) throws ProtocolException, IOException {
        tusClient.setUploadCreationURL(new URL(uploadPath));
        // Enable resumable uploads by storing the upload URL in memory
        tusClient.enableResuming(new TusURLMemoryStore());
        Map<String, String> headers = Map.ofEntries(
                Map.entry("x-api-key", apiKey));
        tusClient.setHeaders(headers);
        final StringWriter sessionIDWriter = new StringWriter();
        TusExecutor executor = new TusExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {

                // First try to resume an upload. If that's not possible we will create a new
                // upload and get a TusUploader in return. This class is responsible for opening
                // a connection to the remote server and doing the uploading.
                TusUploader uploader = tusClient.resumeOrCreateUpload(tusUpload);

                // Alternatively, if your tus server does not support the Creation extension
                // and you obtained an upload URL from another service, you can instruct
                // tus-java-client to upload to a specific URL. Please note that this is usually
                // _not_ necessary and only if the tus server does not support the Creation
                // extension. The Vimeo API would be an example where this method is needed.
                // TusUploader uploader = client.beginOrResumeUploadFromURL(upload, new URL("https://tus.server.net/files/my_file"));

                // Using same size defined in Galaxy code
                uploader.setChunkSize(DEFAULT_CHUNK_SIZE);

                // Upload the file as long as data is available. Once the
                // file has been fully uploaded the method will return -1
                do {
                    // Calculate the progress using the total size of the uploading file and
                    // the current offset.
                    long totalBytes = tusUpload.getSize();
                    long bytesUploaded = uploader.getOffset();
                    double progress = (double) bytesUploaded / totalBytes * 100;
                    log.debug(String.format("Upload at %06.2f%%.\n", progress));

                } while (uploader.uploadChunk() > -1);

                // Allow the HTTP connection to be closed and cleaned up
                uploader.finish();

                log.debug("TUS Upload finished.");
                String uploadFullPath = uploader.getUploadURL().toString();
                String sessionID = uploadFullPath.substring(uploadFullPath.lastIndexOf('/') + 1);
                sessionIDWriter.append(sessionID);
                log.info(String.format("TUS Upload to Galaxy available at: %s", uploader.getUploadURL().toString()));
            }
        };
        executor.makeAttempts();
        return sessionIDWriter.toString();
    }
}
