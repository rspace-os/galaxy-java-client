package com.researchspace.galaxy.model.input.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDatasetCollectionOfPairsRequest {

  @JsonProperty("history_id")
  private final String historyId;

  @JsonProperty("collection_type")
  private final String collectionType = "list:paired";

  @JsonProperty("copy_elements")
  private final boolean copyElements = true;

  @JsonProperty("name")
  private final String nameListOfPair;

  @JsonProperty("element_identifiers")
  private final List<ElementIdentifier> pairedCollection;

  @JsonProperty("hide_source_items")
  private final boolean hideSourceItems = false;

  @JsonProperty("instance_type")
  private final String instanceType = "history";

  private final String type = "dataset_collection";

  /**
   * Note that the creation will fail if the elements in a pair are not called 'forward' and 'reverse'
   * @param nameListOfPair
   * @param collectionName
   * @param datasetForwardId
   * @param datasetReverseId
   * @param historyId
   */
  public CreateDatasetCollectionOfPairsRequest(String nameListOfPair, String collectionName,
      String datasetForwardId, String datasetReverseId, String historyId) {
    this.nameListOfPair = nameListOfPair;
    this.historyId = historyId;
    this.pairedCollection = List.of(
        new ElementIdentifier(collectionName, "new_collection", "paired",
            List.of(
                new ElementIdentifier("forward", datasetForwardId, "hda"),
                new ElementIdentifier("reverse", datasetReverseId, "hda"))
        )
    );
  }

}
