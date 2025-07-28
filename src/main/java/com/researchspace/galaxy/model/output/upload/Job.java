package com.researchspace.galaxy.model.output.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", timezone = "UTC")
  @JsonProperty("update_time")
  private Date updateTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", timezone = "UTC")
  @JsonProperty("create_time")
  private Date createTime;

  @JsonProperty("galaxy_version")
  private String galaxyVersion;

  @JsonProperty("tool_id")
  private String toolId;

  @JsonProperty("tool_version")
  private String toolVersion;

  @JsonProperty("history_id")
  private String historyId;

}
