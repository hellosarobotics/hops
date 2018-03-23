package com.sarobotics.actions;

import com.pi4j.io.gpio.*;
import com.sarobotics.utils.InvalidOpenParachuteAltitude;
import org.apache.log4j.Logger;

public class ActionHW extends Action {

  private final Logger log = Logger.getLogger(ActionHW.class);

  // provision gpio pin #01 as an output pin and turn on
  private GpioPinDigitalOutput detach, deployParachute;

  public ActionHW(int _detachAltitude, int _openParachuteAltitude) throws InvalidOpenParachuteAltitude {
    super(_detachAltitude, _openParachuteAltitude);
    // create gpio controller
    GpioController gpio = GpioFactory.getInstance();
    detach  =         gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "detach", PinState.LOW);
    deployParachute = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "deploy", PinState.LOW);
    detach.setShutdownOptions(true, PinState.LOW);
    deployParachute.setShutdownOptions(true, PinState.LOW);

  }

  public void sganciaSonda() {
    //sgancia
    //Accendiamo il primo led
    log.info("********* DETACH *********");
    detach.high();
  }

  public void apriParacadute() {
    //apri
    //Accendiamo il secondo led
    log.info("********* OPEN PARACHUTE *********");
    deployParachute.high();
  }
}
