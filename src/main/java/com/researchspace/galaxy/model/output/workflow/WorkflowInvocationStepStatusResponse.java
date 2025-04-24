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
public class WorkflowInvocationStepStatusResponse {

  @JsonProperty("model_class")
  private String modelClass;

  @JsonProperty("id")
  private String invocationId;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("create_time")
  private Long createTime;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("update_time")
  private Long updateTime;

  @JsonProperty("workflow_id")
  private String workflowId;

  @JsonProperty("history_id")
  private String historyId;
  private String uuid;
  private String state;

  private List<WorkflowInvocationStep> steps;

  // i.e.: "4" -> { object }
  private Map<String, WorkflowInvocationStepInput> inputs;

  @JsonProperty("input_step_parameters")
  private InputStepParameters inputStepParameters;

  // i.e.: "Small MultiQC stats" -> { object }
  private Map<String, StepOutputItem> outputs;

  // "Mapped Reads" -> { object }
  @JsonProperty("output_collections")
  private Map<String, StepOutputItem> outputCollections;

  @JsonProperty("output_values")
  private Object outputValues;

  private List<String> messages;

}
