package com.researchspace.galaxy.model.input.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Element {

  private String src = "files";
  private String dbkey = "?";
  private String ext = "auto";
  private String name;

  @JsonProperty("space_to_tab")
  private boolean spaceToTab = false;

  @JsonProperty("to_posix_lines")
  private boolean toPosixLines = true;

  private boolean deferred = false;

  public Element(String fileName) {
    this.name = fileName;
  }
}
