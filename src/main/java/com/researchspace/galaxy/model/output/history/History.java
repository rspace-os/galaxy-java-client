package com.researchspace.galaxy.model.output.history;

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

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("create_time")
  private Long createTime;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("update_time")
  private Long updateTime;

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
