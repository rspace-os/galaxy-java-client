package com.researchspace.galaxy.model.input.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatasetInput {

  private final boolean batch = false;
  private final boolean product = false;

  private final List<DatasetValue> values;

  protected DatasetInput(String datasetId){
    this.values = List.of(new DatasetValue(datasetId));
  }

  protected DatasetInput(String datasetId, String src){
    this.values = List.of(new DatasetValue(datasetId, src));
  }

}
