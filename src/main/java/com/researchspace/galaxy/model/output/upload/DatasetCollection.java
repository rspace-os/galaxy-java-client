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
public class DatasetCollection {

  @JsonProperty("model_class")
  private String modelClass;

  private String id;

  @JsonProperty("collection_type")
  private String collectionType;

  private boolean populated;

  @JsonProperty("element_count")
  private int elementCount;

  private List<DatasetCollectionElement> elements;
  private String state;

  @JsonProperty("hda_ldda")
  private String hdaLdda;

  @JsonProperty("history_id")
  private String historyId;

  private List<Object> tags;
  private boolean purged;
  private String uuid;
  private int hid;

  @JsonProperty("file_ext")
  private String fileExt;

  private String peek;
  private String name;
  private boolean deleted;
  private boolean visible;

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
  public Object validatedStateMessage;

  @JsonProperty("misc_info")
  public String miscInfo;

  @JsonProperty("misc_blurb")
  public String miscBlurb;

  @JsonProperty("metadata_dbkey")
  public Object metadataDbkey;

  @JsonProperty("metadata_data_lines")
  public Object metadataDataLines;

  @JsonProperty("metadata_sequences")
  public Object metadataSequences;

}
