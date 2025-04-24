package com.researchspace.galaxy.model.output.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.researchspace.core.util.jsonserialisers.ISO8601DateTimeDeserialiser;
import com.researchspace.core.util.jsonserialisers.ISO8601DateTimeSerialiser;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

  @JsonProperty("model_class")
  private String modelClass;

  private String id;
  private String state;

  @JsonProperty("exit_code")
  private Object exitCode;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("update_time")
  private Long updateTime;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("create_time")
  private Long createTime;

  @JsonProperty("galaxy_version")
  private String galaxyVersion;

  @JsonProperty("tool_id")
  private String toolId;

  @JsonProperty("tool_version")
  private String toolVersion;

  @JsonProperty("history_id")
  private String historyId;

}
