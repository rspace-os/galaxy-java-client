package com.researchspace.galaxy.model.input.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadFileRequest {

  @JsonProperty("history_id")
  private final String historyId;

  private final List<Target> targets;

  @JsonProperty("auto_decompress")
  private final boolean autoDecompress = true;

  @JsonProperty("files_0|file_data")
  private final FileData fileData;

  public UploadFileRequest(String historyId, String sessionId, String fileName) {
    this.historyId = historyId;
    this.fileData = new FileData(sessionId, fileName);
    this.targets = List.of(new Target(fileName));
  }

}
