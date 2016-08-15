package org.camunda.bpm.scenarios;


import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricActivityInstance;

import java.util.Map;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class EventBasedGatewayWaitstate extends Waitstate<EventBasedGatewayWaitstate.EventBasedGateway> {

  protected EventBasedGatewayWaitstate(ProcessEngine processEngine, HistoricActivityInstance instance) {
    super(processEngine, instance);
  }

  protected class EventBasedGateway {

  }

  @Override
  protected EventBasedGateway get() {
    return new EventBasedGateway();
  }

  protected static String getActivityType() {
    return "eventBasedGateway";
  }

  @Override
  protected void execute(Scenario scenario) {
    scenario.atEventBasedGateway(getActivityId()).execute(this);
  }

  protected void leave() {
    throw new UnsupportedOperationException();
  }

  protected void leave(Map<String, Object> variables) {
    throw new UnsupportedOperationException();
  }

}
