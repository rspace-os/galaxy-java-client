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
    new_, waiting, queued, running, resubmitted, submitted, upload, paused;

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
    stopped, stop, ok, skipped;

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
    Running,
    Complete,
    Cancelled,
    Failed,
    Paused,
    Unknown
  }

  /**
   * Any error or deleted jobs signal overall failure or cancellation Cancellation takes precedence
   * over error and both than precedence over running which takes precedence over complete
   */
  @SneakyThrows
  public OverAllState getState() {
    OverAllState toReturn = null;
    Field[] allFields = this.getClass().getDeclaredFields();
    for (Field field : allFields) {
      if (CanceledStates.contains(field.getName())) {
        if (field.getInt(this) > 0) {
          toReturn = OverAllState.Cancelled;
        }
      } else if (ErrorStates.contains(field.getName())) {
        if (field.getInt(this) > 0) {
          if (toReturn != OverAllState.Cancelled) {
            toReturn = OverAllState.Failed;
          }
        }
      } else if (NonTerminalStates.contains(field.getName())) {
        if (field.getInt(this) > 0) {
          if (toReturn != OverAllState.Cancelled && toReturn != OverAllState.Failed) {
            if (field.getName().equals("paused") && toReturn != OverAllState.Running) {
              toReturn = OverAllState.Paused;
            } else {
              toReturn = OverAllState.Running;
            }
          }
        }
      } else if (TerminalStates.contains(field.getName())) {
        if (field.getInt(this) > 0) {
          if (toReturn == null) {
            toReturn = OverAllState.Complete;
          }
        }
      }
    }
    return toReturn != null ? toReturn : OverAllState.Unknown;
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
