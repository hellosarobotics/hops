package com.sarobotics.actions;

import com.sarobotics.utils.InvalidOpenParachuteAltitude;
import org.apache.log4j.Logger;

public class ActionSimulator extends Action {

  Logger log = Logger.getLogger(ActionHW.class);

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
