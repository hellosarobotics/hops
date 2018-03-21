package com.sarobotics.actions;

import com.pi4j.io.gpio.*;
import com.sarobotics.utils.InvalidOpenParachuteAltitude;

public class ActionHW extends Action {

  // create gpio controller
  private GpioController gpio;

  // provision gpio pin #01 as an output pin and turn on
  private GpioPinDigitalOutput detach, deployParachute;

  public ActionHW(int _burstAltitude, int _openParachuteAltitude) throws InvalidOpenParachuteAltitude {
    super(_burstAltitude, _openParachuteAltitude);
    // create gpio controller
    gpio = GpioFactory.getInstance();
    detach  =         gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "detach", PinState.LOW);
    deployParachute = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "deploy", PinState.LOW);
    detach.setShutdownOptions(true, PinState.LOW);
    deployParachute.setShutdownOptions(true, PinState.LOW);

  }

  public void sganciaSonda() {
    //sgancia
    //Accendiamo la luce gialla
    System.out.println("Detach");
    detach.high();
  }

  public void apriParacadute() {
    //apri
    //Accendiamo la luce verde
    System.out.println("Open parachute");
    deployParachute.high();
  }
}
