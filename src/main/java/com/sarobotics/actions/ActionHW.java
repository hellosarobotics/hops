package com.sarobotics.actions;

import com.sarobotics.utils.InvalidOpenParachuteAltitude;

public class ActionHW extends Action {

  public ActionHW(int _burstAltitude, int _openParachuteAltitude) throws InvalidOpenParachuteAltitude {
    super(_burstAltitude, _openParachuteAltitude);
  }

  public void sganciaSonda() {
    //sgancia
  }

  public void apriParacadute() {
    //apri
  }
}
