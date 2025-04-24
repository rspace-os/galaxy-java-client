package com.researchspace.galaxy.model.input.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatasetValue {

  @JsonProperty("id")
  private final String datasetId;

  private final String src;

  @JsonProperty("map_over_type")
  private final Object mapOverType = null;

  protected DatasetValue(String datasetId){
    this.datasetId = datasetId;
    this.src = "hda";
  }

  protected DatasetValue(String datasetId, String src){
    this.datasetId = datasetId;
    this.src = src;
  }

}
