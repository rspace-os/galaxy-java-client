package com.researchspace.galaxy.model.input.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class WorkflowInvocationRequest {

  @JsonProperty("inputs_by")
  private final String inputsBy = "step_index";

  private final boolean batch = true;

  @JsonProperty("use_cached_job")
  private final boolean useCachedJob = false;

  @JsonProperty("require_exact_tool_versions")
  private final boolean requireExactToolVersions = false;

  @JsonProperty("history_id")
  private final String historyId;

  protected WorkflowInvocationRequest(String historyId){
    this.historyId = historyId;
  }

}
