package com.researchspace.galaxy.client;

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
import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

public interface GalaxyClient {

    /**
     * Sanity check - test the connection to Galaxy
     *
     * @param apiKey
     * @return true if the server is existing and is up and running and api-key is correct
     * @throws HttpServerErrorException
     * @throws ResourceAccessException
     */
    boolean testConnection(String apiKey) throws HttpServerErrorException, ResourceAccessException;

    /**
     * Create a new history
     *
     * @param apiKey
     * @return the new History_ID created
     * @throws HttpServerErrorException
     */
    History createNewHistory(String apiKey, String historyName) throws HttpServerErrorException;

    /***
     * Upload a file into Galaxy
     *
     * @param apiKey
     * @param fieToUpload
     * @return the UploadFileOutput object describing the uploaded file
     * @throws HttpServerErrorException
     */
    UploadFileResponse uploadFile(String historyID, String apiKey, File fieToUpload) throws HttpServerErrorException;


  @SneakyThrows
  HistoryDatasetAssociation putAnnotationOnDataset(String historyID, String datasetId,
      String annotation, String apiKey)
      throws HttpServerErrorException;

  HistoryDatasetCollectionAssociation createDatasetCollectionOfPairs(String apiKey, String historyId, String collectionNameOfListOfPair,String pairName,
            String datasetIdForward, String datasetIdReverse)
            throws HttpServerErrorException;


  HistoryDatasetCollectionAssociation createDatasetCollection(String apiKey, String historyId,
      String collectionName,
      Map<String, String> dataFileNamesToIds) throws HttpServerErrorException;

  /***
     * Using the given datasetID, invokes a workflow
     * @param apiKey
     * @param workflowId
     * @return a response having the identification of the invocation
     * @throws HttpServerErrorException
     */
    List<WorkflowInvocationResponse> invokeWorkflow(String apiKey, WorkflowInvocationRequest request,String workflowId)
            throws HttpServerErrorException;

    /**
     * Returns all invocations for the given history ID, excluding nested invocations
     * @param apiKey
     * @param historyId
     * @return
     */
    List<WorkflowInvocationResponse> getTopLevelInvocationsInAHistory(String apiKey, String historyId);


    /***
     *  Summary state of specific workflow invocation
     *
     * @param apiKey
     * @param invocationId
     * @return a response describing the overall/summary status of the workflow
     * @throws HttpServerErrorException
     */
    WorkflowInvocationSummaryStatusResponse getWorkflowInvocatioSummaryStatus(String apiKey,
            String invocationId)
            throws HttpServerErrorException;

    /**
     * Gives detailed information about a workflow invocation, including which datasets were used as 'inputs'
     * @param apiKey
     * @param invocationId
     * @return
     */
    WorkflowInvocationStepStatusResponse getWorkflowInvocationData(String apiKey, String invocationId);

  /**
   * The report for an invocation contains the 'title' which is the invoked workflow name.
   * @param apiKey
   * @param invocationId
   * @return
   */
   WorkflowInvocationReport getWorkflowInvocationReport(String apiKey,
      String invocationId);

  /**
   * For collection datasets associated with a history
   */
  HistoryDatasetCollectionAssociation getDataSetCollectionDetails(String apiKey,
      String historyId, String dataSetId);

  /**
   * For datasets that are not part of collections
   */
  DataSet getDataSetDetails(String apiKey, String dataSetId);
}
