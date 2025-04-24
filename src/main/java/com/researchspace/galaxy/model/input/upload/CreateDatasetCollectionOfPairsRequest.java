package com.researchspace.galaxy.model.input.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDatasetCollectionOfPairsRequest {

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

  private final Object options = new Object();

  @JsonProperty("instance_type")
  private final String instanceType = "history";

  private final String type = "dataset_collection";

  public CreateDatasetCollectionOfPairsRequest(String nameListOfPair, String collectionName,
      String datasetForwardId, String datasetReverseId) {
    this.nameListOfPair = nameListOfPair;
    this.pairedCollection = List.of(
        new ElementIdentifier(collectionName, "new_collection", "paired",
            List.of(
                new ElementIdentifier("forward", datasetForwardId, "hda"),
                new ElementIdentifier("reverse", datasetReverseId, "hda"))
        )
    );
  }

}
