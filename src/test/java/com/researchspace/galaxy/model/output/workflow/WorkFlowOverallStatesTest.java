package com.researchspace.galaxy.model.output.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.researchspace.galaxy.model.output.workflow.WorkflowOverallStates.OverAllState;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class WorkFlowOverallStatesTest {

  @Test
  public void testCancelledState(){
    WorkflowOverallStates wos = setupWos();
    wos.setDeleted(1);
    assertEquals(OverAllState.Cancelled, wos.getState());
  }

  private static @NotNull WorkflowOverallStates setupWos() {
    WorkflowOverallStates wos = new WorkflowOverallStates();
    wos.setOk(100);
    wos.setQueued(10);
    wos.setRunning(10);
    wos.setSkipped(100);
    return wos;
  }

  @Test
  public void testFailedState(){
    WorkflowOverallStates wos = setupWos();
    wos.setError(1);
    assertEquals(OverAllState.Failed, wos.getState());
  }
  @Test
  public void testInprogressState(){
    WorkflowOverallStates wos = setupWos();
    assertEquals(OverAllState.Running, wos.getState());
  }
  @Test
  public void testCompleteState(){
    WorkflowOverallStates wos = new WorkflowOverallStates();
    wos.setOk(100);
    assertEquals(OverAllState.Complete, wos.getState());
  }
}
