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
public class HistoryDatasetCollectionAssociation {

  @JsonProperty("model_class")
  private String modelClass;

  @JsonProperty("id")
  private String datasetId;

  private String name;

  @JsonProperty("history_id")
  private String historyId;

  private int hid;
  private boolean deleted;
  private boolean visible;
  private String type;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("create_time")
  private Long createTime;

  @JsonSerialize(using = ISO8601DateTimeSerialiser.class)
  @JsonDeserialize(using = ISO8601DateTimeDeserialiser.class)
  @JsonProperty("update_time")
  private Long updateTime;

  private String url;

  private List<Object> tags;

  @JsonProperty("history_content_type")
  private String historyContentType;

  @JsonProperty("collection_type")
  private String collectionType;

  @JsonProperty("populated_state")
  private String populatedState;

  @JsonProperty("populated_state_message")
  private String populatedStateMessage;

  @JsonProperty("element_count")
  private int elementCount;

  @JsonProperty("elements_datatypes")
  private List<String> elementsDatatypes;

  @JsonProperty("job_source_id")
  private String jobSourceId;

  @JsonProperty("job_source_type")
  private String jobSourceType;

  @JsonProperty("job_state_summary")
  private JobStateSummary jobStateSummary;

  @JsonProperty("contents_url")
  private String contentsUrl;

  @JsonProperty("collection_id")
  private String collectionId;

  private boolean populated;
  private List<DatasetCollectionElement> elements;

  @JsonProperty("implicit_collection_jobs_id")
  private String implicitCollectionJobsId;

}
