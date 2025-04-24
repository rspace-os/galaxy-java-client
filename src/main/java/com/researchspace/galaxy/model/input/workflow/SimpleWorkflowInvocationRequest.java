package com.researchspace.galaxy.model.input.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.researchspace.galaxy.client.GalaxyClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleWorkflowInvocationRequest extends WorkflowInvocationRequest {

  @JsonIgnore
  private final String id = GalaxyClient.SIMPLE_WORKFLOW_ID;

  private final SimpleInputs inputs;

  public SimpleWorkflowInvocationRequest(String historyId, String datasetId) {
    super(historyId);
    this.inputs = new SimpleInputs(datasetId);
  }

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SimpleInputs {

    @JsonProperty("0")
    private final DatasetInput datasetInput;

    protected SimpleInputs(String datasetId){
      this.datasetInput = new DatasetInput(datasetId);
    }

  }

}
