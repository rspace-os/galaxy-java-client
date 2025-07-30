package com.researchspace.galaxy.model.output.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.Field;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowOverallStates {

  public enum NonTerminalStates {
    new_, waiting, queued, running, resubmitted, upload;

    public static boolean contains(String test) {
      for (NonTerminalStates c : NonTerminalStates.values()) {
        if (c.name().equals(test)) {
          return true;
        }
      }
      return false;
    }
  }

  public enum ErrorStates {
    error, failed;

    public static boolean contains(String test) {
      for (ErrorStates c : ErrorStates.values()) {
        if (c.name().equals(test)) {
          return true;
        }
      }
      return false;
    }
  }

  public enum TerminalStates {
    paused, stopped, stop, ok, skipped;

    public static boolean contains(String test) {
      for (TerminalStates c : TerminalStates.values()) {
        if (c.name().equals(test)) {
          return true;
        }
      }
      return false;
    }
  }

  public enum CanceledStates {
    deleted, deleting;

    public static boolean contains(String test) {
      for (CanceledStates c : CanceledStates.values()) {
        if (c.name().equals(test)) {
          return true;
        }
      }
      return false;
    }
  }

  public enum OverAllState {
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    FAILED;
  }

  @SneakyThrows
  public OverAllState getState() {
    Field[] allFields = this.getClass().getDeclaredFields();
    for (int i = 0; i < allFields.length; i++) {
      Field field = allFields[i];
      if (ErrorStates.contains(field.getName())) {
        if (field.getInt(this) > 0) {
          return OverAllState.FAILED;
        }
      }
      if (CanceledStates.contains(field.getName())) {
        if (field.getInt(this) > 0) {
          return OverAllState.CANCELLED;
        }
      }
      else if (NonTerminalStates.contains(field.getName())) {
        if (field.getInt(this) > 0) {
          return OverAllState.IN_PROGRESS;
        }
      } else if (TerminalStates.contains(field.getName())) {
        if (field.getInt(this) > 0) {
          return OverAllState.COMPLETED;
        }
      }

    }
  }

  private int ok;
  private int queued;
  private int running;
  private int error;
  private int deleted;
  private int deleting;
  @JsonProperty("new")
  private int new_;
  private int paused;
  private int skipped;
  private int resubmitted;
  private int submitted;
  private int waiting;
  private int failed;
  private int upload;
  private int stopped;
  private int stop;

}
