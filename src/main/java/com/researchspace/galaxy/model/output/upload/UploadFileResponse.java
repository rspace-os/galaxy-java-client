package com.researchspace.galaxy.model.output.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadFileResponse {

  private List<HistoryDatasetAssociation> outputs;

  @JsonProperty("output_collections")
  private List<Object> outputCollections;

  private List<Job> jobs;

  @JsonProperty("implicit_collections")
  private List<Object> implicitCollections;

  @JsonProperty("produces_entry_points")
  private boolean producesEntryPoints;

}
