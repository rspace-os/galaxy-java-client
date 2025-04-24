package com.researchspace.galaxy.model.input.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.researchspace.galaxy.client.GalaxyClient;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PEWorkflowInvocationRequest extends WorkflowInvocationRequest {

  @JsonIgnore
  private final String id = GalaxyClient.PE_WORKFLOW_ID;

  private final PEInputs inputs;

  public PEWorkflowInvocationRequest(String historyId, String datasetId) {
    super(historyId);
    this.inputs = new PEInputs(datasetId);
  }

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PEInputs {

    private static final String INTERNAL_HDCA = "4c43511826fb67eb";

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
      this.zero = new DatasetInput(INTERNAL_HDCA, "hdca");
      this.fifth = new DatasetInput(datasetId);
    }
  }

}
