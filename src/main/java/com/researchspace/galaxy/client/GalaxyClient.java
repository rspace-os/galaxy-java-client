package com.researchspace.galaxy.client;

import com.researchspace.galaxy.model.output.history.History;
import com.researchspace.galaxy.model.output.upload.HistoryDatasetCollectionAssociation;
import com.researchspace.galaxy.model.output.upload.UploadFileResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationStepStatusResponse;
import com.researchspace.galaxy.model.output.workflow.WorkflowInvocationSummaryStatusResponse;
import java.io.File;
import java.io.IOException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

/*
 * This interface declares the operations that this library supports with
 * regard to the Galaxy API.
 */
public interface GalaxyClient {

  String PE_WORKFLOW_ID = "2d08a73dd8ff99e9";
  String SE_WORKFLOW_ID = "87fea062a9646a31";
  String SIMPLE_WORKFLOW_ID = "992cc472e5f300f1";


  /**
   * This method is in charge to test the connection
   *
   * @param apiKey
   * @return true if the server is existing and is up and running and api-key is correct
   * @throws HttpServerErrorException
   * @throws ResourceAccessException
   */
  boolean testConnection(String apiKey) throws HttpServerErrorException, ResourceAccessException;

  /**
   * This method is in charge to create a new history
   *
   * @param apiKey
   * @return the new History_ID created
   * @throws HttpServerErrorException
   */
  History createNewHistory(String apiKey, String historyName) throws HttpServerErrorException;

  /***
   * This method is in charge to upload a file into Galaxy
   *
   * @param apiKey
   * @param fieToUpload
   * @return the UploadFileOutput object describing the uploaded file
   * @throws HttpServerErrorException
   */
  UploadFileResponse uploadFile(String apiKey, File fieToUpload) throws HttpServerErrorException;


  HistoryDatasetCollectionAssociation createDatasetCollectionOfPairs(String apiKey,
      String historyId, String nameListOfPair, String collectionName,
      String datasetIdForward, String DataserIdReverse)
      throws HttpServerErrorException;


  /***
   * This method is in charge to invoke a specific workflow giving a given dataset as input
   *
   * @param apiKey
   * @param workflowId
   * @param datasetId
   * @return a response having the identification of the invocation
   * @throws HttpServerErrorException
   */
  WorkflowInvocationResponse invokeWorkflow(String apiKey, String historyId,
      String workflowId, String datasetId)
      throws HttpServerErrorException;


  /***
   *  This method is in charge to check the status of a specific workflow invocation
   *
   * @param apiKey
   * @param invocationId
   * @return a response describing the overall/summary status of the workflow
   * @throws HttpServerErrorException
   */
  WorkflowInvocationSummaryStatusResponse getWorkflowInvocationOverallStatus(String apiKey,
      String invocationId)
      throws HttpServerErrorException;

  /***
   *  This method is in charge to check the status of a specific workflow invocation
   *
   * @param apiKey
   * @param invocationId
   * @return a response describing step by step
   * @throws HttpServerErrorException
   */
  WorkflowInvocationStepStatusResponse getWorkflowStepsInvocationStatus(String apiKey,
      String invocationId)
      throws HttpServerErrorException;

  /***
   * This method is in charge to get the link for a specific history where a workflow
   * invocation ran
   *
   * @param apiKey
   * @param invocationId
   * @return the file result object
   * @throws HttpServerErrorException
   * @throws IOException
   */
  String getHistoryLink(String apiKey, String invocationId)
      throws HttpServerErrorException, IOException;
  // https://usegalaxy.eu/histories/view?id=cbc8d9f78aef06cd


}
