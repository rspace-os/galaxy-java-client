package com.researchspace.galaxy.model.input.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElementIdentifier {

  @JsonProperty("collection_type")
  private final String collectionType;

  private final String id;

  private final String src;

  private final String name;

  @JsonProperty("element_identifiers")
  private List<ElementIdentifier> elementIdentifiers;


  public ElementIdentifier(String name, String id, String src) {
    this.name = name;
    this.id = id;
    this.src = src;
    this.elementIdentifiers = null;
    this.collectionType = null;
  }

  public ElementIdentifier(String name, String src, String collectionType,
      List<ElementIdentifier> elementIdentifiers) {
    this.name = name;
    this.id = null;
    this.src = src;
    this.elementIdentifiers = elementIdentifiers;
    this.collectionType = collectionType;
  }

}
