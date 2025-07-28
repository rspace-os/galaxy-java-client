package com.researchspace.galaxy.model.output.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkFlowMessage {

  private String reason;
  private int workflow_step_id;
  private String hda_id;
  private long dependent_workflow_step_id;

}
