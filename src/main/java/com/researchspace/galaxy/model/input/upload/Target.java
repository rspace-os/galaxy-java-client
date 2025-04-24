package com.researchspace.galaxy.model.input.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Target {

  private final Destination destination = new Destination();
  private final List<Element> elements;

  public Target(String fileName){
    this.elements = List.of(new Element(fileName));
  }

  @Getter
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Destination {

    private final String type = "hdas";
  }

}
