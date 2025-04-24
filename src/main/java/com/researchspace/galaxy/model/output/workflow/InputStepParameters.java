package com.researchspace.galaxy.model.output.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputStepParameters {

  @JsonProperty("parameter_value")
  private boolean parameterValue;

  private String label;

  @JsonProperty("workflow_step_id")
  private String workflowStepId;

}
