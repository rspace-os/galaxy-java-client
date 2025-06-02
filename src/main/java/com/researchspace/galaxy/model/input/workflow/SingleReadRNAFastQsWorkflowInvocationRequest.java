package com.researchspace.galaxy.model.input.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * This class is hard coded to work with the Galaxy workflow with ID 87fea062a9646a31 :
 * RNA-seq for Single-read fastqs (release v1.2)
 * The hard coded values are purely examples of what can be used
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleReadRNAFastQsWorkflowInvocationRequest extends WorkflowInvocationRequest {

  @JsonIgnore
  private final String forWorkFlowWithId = "87fea062a9646a31";

  private final SEInputs inputs;

  public SingleReadRNAFastQsWorkflowInvocationRequest(String historyId, String datasetId) {
    super(historyId);
    this.inputs = new SEInputs(datasetId);
  }

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SEInputs {

    private static final String INTERNAL_HDA = "4838ba20a6d8676517a6a029b53330a0";

    @JsonProperty("0")
    private final DatasetInput zero;

    @JsonProperty("1")
    private final Object first = null;

    @JsonProperty("2")
    private final boolean second = false;

    @JsonProperty("3")
    private final String third = "apiMel3";

    @JsonProperty("4")
    private final DatasetInput fourth;

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
      this.fourth = new DatasetInput(INTERNAL_HDA, "hda");
      this.zero = new DatasetInput(datasetId,"hdca");
    }

  }

}
