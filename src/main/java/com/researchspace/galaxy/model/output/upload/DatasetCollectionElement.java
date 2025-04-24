package com.researchspace.galaxy.model.output.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatasetCollectionElement {

  @JsonProperty("model_class")
  private String modelClass;

  private String id;

  @JsonProperty("element_index")
  private int elementIndex;

  @JsonProperty("element_identifier")
  private String elementIdentifier;

  @JsonProperty("element_type")
  private String elementType;

  private DatasetCollection object;

}
