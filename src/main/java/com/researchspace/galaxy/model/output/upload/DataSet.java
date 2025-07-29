package com.researchspace.galaxy.model.output.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSet {

  private String annotation;
  private String url;
  @JsonProperty("dataset_id")
  private String datasetId;
  private String uuid;
  private String name;

}
