package com.sarobotics.actions;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.sarobotics.bmp280.BMP280;
import com.sarobotics.utils.InvalidOpenParachuteAltitude;
import com.sarobotics.utils.PCA9685;
import org.apache.log4j.Logger;

public class ActionHW extends Action {

  private final Logger log = Logger.getLogger(ActionHW.class);

  private DigitalOutput detach, deployParachute;
  private DigitalInput myButton;
  private BMP280 bmp280;
  private int detachAltitudeParameter, openParachuteAltitudeParameter, cicleTimeParameter;

  private int servoMin = 200;
  private int servoMax = 600;
  private final int DROP_SERVO_CHANNEL = 0;
  private final int PARACHUTE_SERVO_CHANNEL = 1;

  private PCA9685 servoBoard;
  private Context pi4j;

  public ActionHW(int _detachAltitude, int _openParachuteAltitude, BMP280 _bmp280, int _detachAltitudeParameter, int _openParachuteAltitudeParameter, int _cicleTimeParameter) throws InvalidOpenParachuteAltitude {
    super(_detachAltitude, _openParachuteAltitude);

    this.bmp280 = _bmp280;
    this.detachAltitudeParameter = _detachAltitudeParameter;
    this.openParachuteAltitudeParameter = _openParachuteAltitudeParameter;
    this.cicleTimeParameter = _cicleTimeParameter;

    // Pi4J v2 context
    pi4j = Pi4J.newAutoContext();

    // Inizializzazione della servo board
    this.servoBoard = new PCA9685(pi4j);
    servoBoard.setPWMFreq(60);

    // Setup GPIO output pins
    detach = pi4j.create(DigitalOutput.newConfigBuilder(pi4j)
            .id("detach")
            .name("Detach")
            .address(5)
            .shutdown(DigitalState.LOW)
            .initial(DigitalState.LOW)
            .provider("pigpio-digital-output")
            .build());

    deployParachute = pi4j.create(DigitalOutput.newConfigBuilder(pi4j)
            .id("deploy")
            .name("Deploy Parachute")
            .address(6)
            .shutdown(DigitalState.LOW)
            .initial(DigitalState.LOW)
            .provider("pigpio-digital-output")
            .build());

    // Setup GPIO input pin (button)
    myButton = pi4j.create(DigitalInput.newConfigBuilder(pi4j)
            .id("button")
            .name("Reset Button")
            .address(3)
            .pull(PullResistance.PULL_DOWN)
            .provider("pigpio-digital-input")
            .build());

    myButton.addListener(event -> {
      if (event.state() == DigitalState.LOW) {
        reset();
      }
    });
  }

  private void reset() {
    log.info("********* RESET *********");
    detach.low();
    deployParachute.low();
    deveAncoraScoppiare = true;
    ilParacaduteSiDeveAncoraAprire = true;
    int currentAltitude = 0;
    try {
      currentAltitude = (int) bmp280.getAltitudeInMeter();
      detachAltitude = currentAltitude + detachAltitudeParameter;
      openParachuteAltitude = currentAltitude + openParachuteAltitudeParameter;
      servoBoard.deployParachute(PARACHUTE_SERVO_CHANNEL, servoMin);
    } catch (Exception e) {
      e.printStackTrace();
    }

    log.info("Parametri al RESET");
    log.info("Start Altitude: " + currentAltitude + "m. DETACH at: " + detachAltitude + "m. OpenParachute at: " + openParachuteAltitude + "m. CicleTime: " + cicleTimeParameter + " milliseconds");
  }

  public void sganciaSonda() {
    log.info("********* DETACH *********");
    detach.high();
    servoBoard.inizializzaServoPerIlDrop(DROP_SERVO_CHANNEL, servoMin, servoMax);
    servoBoard.releasePayLoad(DROP_SERVO_CHANNEL, servoMin, servoMax);
  }

  public void apriParacadute() {
    log.info("********* OPEN PARACHUTE *********");
    deployParachute.high();
    servoBoard.deployParachute(PARACHUTE_SERVO_CHANNEL, servoMax);
  }
}
