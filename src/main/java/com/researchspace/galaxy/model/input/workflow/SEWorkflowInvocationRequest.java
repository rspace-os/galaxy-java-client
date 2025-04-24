package com.researchspace.galaxy.model.input.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.researchspace.galaxy.client.GalaxyClient;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SEWorkflowInvocationRequest extends WorkflowInvocationRequest {

  @JsonIgnore
  private final String id = GalaxyClient.SE_WORKFLOW_ID;

  private final SEInputs inputs;

  public SEWorkflowInvocationRequest(String historyId, String datasetId) {
    super(historyId);
    this.inputs = new SEInputs(datasetId);
  }

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SEInputs {

    private static final String INTERNAL_HDCA = "3e6fb6399b460cfb";

    @JsonProperty("0")
    private final DatasetInput zero;

    @JsonProperty("1")
    private final Object first = null;

    @JsonProperty("2")
    private final boolean second = false;

    @JsonProperty("3")
    private final String third = "apiMel3";

    @JsonProperty("4")
    private final DatasetInput forth;

    @JsonProperty("5")
    private final String fifth = "stranded - forward";

    @JsonProperty("6")
    private final boolean sixth = false;

    @JsonProperty("7")
    private final boolean seventh = false;

    @JsonProperty("8")
    private final Object eighth = null;

    @JsonProperty("9")
    private final boolean ninth = false;

    protected SEInputs(String datasetId) {
      this.zero = new DatasetInput(INTERNAL_HDCA, "hdca");
      this.forth = new DatasetInput(datasetId);
    }

  }

}
