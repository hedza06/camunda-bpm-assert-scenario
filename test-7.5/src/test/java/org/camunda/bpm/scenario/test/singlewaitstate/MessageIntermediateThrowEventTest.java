package org.camunda.bpm.scenario.test.singlewaitstate;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.scenario.Scenario;
import org.camunda.bpm.scenario.action.MessageIntermediateThrowEventAction;
import org.camunda.bpm.scenario.runner.MessageIntermediateThrowEventWaitstate;
import org.camunda.bpm.scenario.test.AbstractTest;
import org.junit.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
@Deployment(resources = {"org/camunda/bpm/scenario/test/singlewaitstate/MessageIntermediateThrowEventTest.bpmn"})
public class MessageIntermediateThrowEventTest extends AbstractTest {

  @Test
  public void testCompleteTask() {

    when(scenario.atMessageIntermediateThrowEvent("MessageIntermediateThrowEvent")).thenReturn(new MessageIntermediateThrowEventAction() {
      @Override
      public void execute(MessageIntermediateThrowEventWaitstate externalTask) {
        externalTask.complete();
      }
    });

    Scenario.run(scenario).startBy("MessageIntermediateThrowEventTest").execute();

    verify(scenario, times(1)).hasCompleted("MessageIntermediateThrowEvent");
    verify(scenario, times(1)).hasFinished("EndEvent");

  }

  @Test
  public void testDoNothing() {

    when(scenario.atMessageIntermediateThrowEvent("MessageIntermediateThrowEvent")).thenReturn(new MessageIntermediateThrowEventAction() {
      @Override
      public void execute(MessageIntermediateThrowEventWaitstate externalTask) {
        // Deal with externalTask but do nothing here
      }
    });

    Scenario.run(scenario).startBy("MessageIntermediateThrowEventTest").execute();

    verify(scenario, times(1)).hasStarted("MessageIntermediateThrowEvent");
    verify(scenario, never()).hasFinished("MessageIntermediateThrowEvent");
    verify(scenario, never()).hasFinished("EndEvent");

  }

  @Test(expected=AssertionError.class)
  public void testDoNotDealWithTask() {

    Scenario.run(scenario).startBy("MessageIntermediateThrowEventTest").execute();

  }

  @Test
  public void testToBeforeMessageIntermediateThrowEvent() {

    when(scenario.atMessageIntermediateThrowEvent("MessageIntermediateThrowEvent")).thenReturn(new MessageIntermediateThrowEventAction() {
      @Override
      public void execute(MessageIntermediateThrowEventWaitstate externalTask) {
        externalTask.complete();
      }
    });

    Scenario.run(scenario).startBy("MessageIntermediateThrowEventTest").toBefore("MessageIntermediateThrowEvent").execute();

    verify(scenario, times(1)).hasStarted("MessageIntermediateThrowEvent");
    verify(scenario, never()).hasFinished("MessageIntermediateThrowEvent");
    verify(scenario, never()).hasFinished("EndEvent");

  }

  @Test
  public void testToAfterMessageIntermediateThrowEvent() {

    when(scenario.atMessageIntermediateThrowEvent("MessageIntermediateThrowEvent")).thenReturn(new MessageIntermediateThrowEventAction() {
      @Override
      public void execute(MessageIntermediateThrowEventWaitstate externalTask) {
        externalTask.complete();
      }
    });

    Scenario.run(scenario).startBy("MessageIntermediateThrowEventTest").toAfter("MessageIntermediateThrowEvent").execute();

    verify(scenario, times(1)).hasStarted("MessageIntermediateThrowEvent");
    verify(scenario, times(1)).hasFinished("MessageIntermediateThrowEvent");
    verify(scenario, times(1)).hasFinished("EndEvent");

  }

  @Test
  public void testWhileOtherProcessInstanceIsRunning() {

    when(scenario.atMessageIntermediateThrowEvent("MessageIntermediateThrowEvent")).thenReturn(new MessageIntermediateThrowEventAction() {
      @Override
      public void execute(MessageIntermediateThrowEventWaitstate externalTask) {
        externalTask.complete();
      }
    });

    Scenario.run(otherScenario).startBy("MessageIntermediateThrowEventTest").toBefore("MessageIntermediateThrowEvent").execute();
    Scenario.run(scenario).startBy("MessageIntermediateThrowEventTest").execute();

    verify(scenario, times(1)).hasCompleted("MessageIntermediateThrowEvent");
    verify(scenario, times(1)).hasFinished("EndEvent");
    verify(otherScenario, never()).hasCompleted("MessageIntermediateThrowEvent");

  }

}
