package com.researchspace.galaxy.model.output.history;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class History {

  @JsonProperty("model_class")
  private String modelClass;  public String id;
  private String name;
  private boolean deleted;
  private boolean purged;
  private boolean archived;
  private String url;
  private boolean published;
  private int count;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", timezone = "UTC")
  @JsonProperty("create_time")
  private Date createTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", timezone = "UTC")
  @JsonProperty("update_time")
  private Date updateTime;

  private String contents_url;
  private int size;

  @JsonProperty("user_id")
  private String userId;

  private boolean importable;
  private Object slug;
  private String username;

  @JsonProperty("genome_build")
  private String genomeBuild;

  private String state;
}
