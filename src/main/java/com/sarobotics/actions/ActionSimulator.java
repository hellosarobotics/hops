package com.sarobotics.actions;

import com.sarobotics.utils.InvalidOpenParachuteAltitude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionSimulator extends Action {

	private static final Logger log = LoggerFactory.getLogger(ActionSimulator.class);

  public ActionSimulator(int _detachAltitude, int _openParachuteAltitude) throws InvalidOpenParachuteAltitude {
    super(_detachAltitude, _openParachuteAltitude);
  }

  public void sganciaSonda() {
    log.info("********* DETACH *********");
  }

  public void apriParacadute() {
    log.info("********* OPEN PARACHUTE *********");
  }
}
