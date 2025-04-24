package com.researchspace.galaxy.model.output.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.researchspace.core.util.jsonserialisers.ISO8601DateTimeDeserialiser;
import com.researchspace.core.util.jsonserialisers.ISO8601DateTimeSerialiser;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowInvocationStep {

  @JsonProperty("model_class")
  private String modelClass;

  private String id;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("update_time")
  private Long updateTime;

  @JsonProperty("job_id")
  private String jobId;

  @JsonProperty("workflow_step_id")
  private String workflowStepId;

  @JsonProperty("subworkflow_invocation_id")
  private String subworkflowInvocationId;

  private String state;
  private Object action;

  @JsonProperty("order_index")
  private int orderIndex;

  @JsonProperty("workflow_step_label")
  private String workflowStepLabel;

  @JsonProperty("workflow_step_uuid")
  private String workflowStepUuid;

  private Map<String, StepOutputItem> outputs;

  @JsonProperty("output_collections")
  private Map<String, StepOutputItem> outputCollections;

  private List<Object> jobs;

  @JsonProperty("implicit_collection_jobs_id")
  private String implicit_collection_jobs_id;
}
