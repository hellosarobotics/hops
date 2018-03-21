package com.sarobotics.actions;

import com.sarobotics.utils.InvalidOpenParachuteAltitude;

public class ActionSimulator extends Action {


  public ActionSimulator(int _burstAltitude, int _openParachuteAltitude) throws InvalidOpenParachuteAltitude {
    super(_burstAltitude, _openParachuteAltitude);
  }

  public void sganciaSonda() {
    System.out.println("DETACH");
  }

  public void apriParacadute() {
    System.out.println("OPEN PARACHUTE");
  }
}
