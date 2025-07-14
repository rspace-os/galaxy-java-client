package com.researchspace.galaxy.model.input.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PutAnnotationRequest {
  private String annotation;

  public PutAnnotationRequest(String annotation) {
    this.annotation = annotation;
  }
}
