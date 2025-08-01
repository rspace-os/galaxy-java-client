package com.researchspace.galaxy.model.output.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = { "invocationId"})
public class WorkflowInvocationResponse {

  @JsonProperty("model_class")
  private String modelClass;

  @JsonProperty("id")
  private String invocationId;

  @JsonProperty("create_time")
  private Date createTime;

  @JsonProperty("update_time")
  private Date updateTime;

  @JsonProperty("workflow_id")
  private String workflowId;

  @JsonProperty("history_id")
  private String historyId;
  @JsonProperty("uuid")
  private String uuid;
  @JsonProperty("state")
  private String state;

}
