package com.researchspace.galaxy.model.output.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.researchspace.core.util.jsonserialisers.ISO8601DateTimeDeserialiser;
import com.researchspace.core.util.jsonserialisers.ISO8601DateTimeSerialiser;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryDatasetAssociation {

  @JsonProperty("id")
  private String datasetId;

  @JsonProperty("hda_ldda")
  private String hdaLdda;

  private String uuid;
  private int hid;

  @JsonProperty("file_ext")
  private String fileExt;

  private Object peek;

  @JsonProperty("model_class")
  private String modelClass;

  private String name;
  private boolean deleted;
  private boolean purged;
  private boolean visible;
  private String state;

  @JsonProperty("history_content_type")
  private String historyContentType;

  @JsonProperty("file_size")
  private int fileSize;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("create_time")
  private Long createTime;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("update_time")
  private Long updateTime;

  @JsonProperty("data_type")
  private String dataType;

  @JsonProperty("genome_build")
  private String genomeBuild;

  @JsonProperty("validated_state")
  private String validatedState;

  @JsonProperty("validated_state_message")
  private Object validatedStateMessage;

  @JsonProperty("misc_info")
  private Object miscInfo;

  @JsonProperty("misc_blurb")
  private Object miscBlurb;

  private List<Object> tags;

  @JsonProperty("history_id")
  private String historyId;

  @JsonProperty("metadata_dbkey")
  private String metadataDbkey;

  @JsonProperty("output_name")
  private String outputName;

}
