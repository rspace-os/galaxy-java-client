package com.researchspace.galaxy.model.input.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * This class is hard coded to work with the Galaxy workflow 2d08a73dd8ff99e9 :
 * RNA-seq for Paired-end fastqs (release v1.2)
 * The hard coded values are purely examples of what can be used
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PairedEndRNAFastQsWorkflowInvocationRequest extends WorkflowInvocationRequest {

  @JsonIgnore
  private final String forWorkFlowWithId = "2d08a73dd8ff99e9";

  private final PEInputs inputs;

  public PairedEndRNAFastQsWorkflowInvocationRequest(String historyId, String datasetId) {
    super(historyId);
    this.inputs = new PEInputs(datasetId);
  }

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PEInputs {

    private static final String INTERNAL_HDA = "4838ba20a6d8676517a6a029b53330a0";

    @JsonProperty("0")
    private final DatasetInput zero;

    @JsonProperty("1")
    private final Object first = null;

    @JsonProperty("2")
    private final Object second = null;

    @JsonProperty("3")
    private final boolean third = false;

    @JsonProperty("4")
    private final String forth = "apiMel3";

    @JsonProperty("5")
    private final DatasetInput fifth;

    @JsonProperty("6")
    private final String sixth = "stranded - forward";

    @JsonProperty("7")
    private final boolean seventh = false;

    @JsonProperty("8")
    private final boolean eighth = false;

    @JsonProperty("9")
    private final Object ninth = null;

    @JsonProperty("10")
    private final boolean tenth = false;


    protected PEInputs(String datasetId) {
      this.fifth = new DatasetInput(INTERNAL_HDA, "hda");
      this.zero = new DatasetInput(datasetId,"hdca");
    }
  }

}
