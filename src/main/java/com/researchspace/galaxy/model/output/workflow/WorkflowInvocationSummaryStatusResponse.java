package com.researchspace.galaxy.model.output.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowInvocationSummaryStatusResponse {

  public String id;
  public WorkflowOverallStates states;

  @JsonProperty("populated_state")
  public String populatedState;

  public String model;

}
