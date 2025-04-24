package com.researchspace.galaxy.model.output.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobStateSummary {

  private int all_jobs;

  @JsonProperty("new")
  private int newj;

  private int waiting;
  private int running;
  private int error;
  private int paused;
  private int skipped;
  private int resubmitted;
  private int queued;
  private int ok;
  private int failed;
  private int deleted;
  private int upload;

}
