package com.researchspace.galaxy.model.input.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDatasetCollectionRequest {

  @JsonProperty("history_id")
  private final String historyId;

  @JsonProperty("collection_type")
  private final String collectionType = "list";

  @JsonProperty("copy_elements")
  private final boolean copyElements = true;

  @JsonProperty("name")
  private final String collectionName;

  @JsonProperty("element_identifiers")
  private final List<ElementIdentifier> data;

  @JsonProperty("hide_source_items")
  private final boolean hideSourceItems = false;

  @JsonProperty("instance_type")
  private final String instanceType = "history";

  private final String type = "dataset_collection";

  public CreateDatasetCollectionRequest(String collectionName, String dataFileName,
      String dataId, String historyId) {
    this.collectionName = collectionName;
    this.data = List.of(
        new ElementIdentifier(dataFileName,dataId,"hda")
    );
    this.historyId = historyId;
  }

}
