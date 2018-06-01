package com.sarobotics.actions;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CFactory;
import com.sarobotics.bmp280.BMP280;
import com.sarobotics.utils.InvalidOpenParachuteAltitude;
import com.sarobotics.utils.PCA9685;
import org.apache.log4j.Logger;

public class ActionHW extends Action {

  private final Logger log = Logger.getLogger(ActionHW.class);

  // provision gpio pin #01 as an output pin and turn on
  private GpioPinDigitalOutput detach, deployParachute;
  private GpioPinDigitalInput myButton;
  private BMP280 bmp280;
  private int detachAltitudeParameter,openParachuteAltitudeParameter,cicleTimeParameter;

  private int servoMin = 122; // 130;   // was 150. Min pulse length out of 4096
  private int servoMax = 615;   // was 600. Max pulse length out of 4096
  private final int DROP_SERVO_CHANNEL = 0;

  /* Inizializzazione Servo board*/
  private PCA9685 servoBoard;
  {
    try {
      servoBoard = new PCA9685();
    } catch (I2CFactory.UnsupportedBusNumberException e) {
      e.printStackTrace();
    }
  }


  public ActionHW(int _detachAltitude, int _openParachuteAltitude, BMP280 _bmp280, int _detachAltitudeParameter, int _openParachuteAltitudeParameter, int _cicleTimeParameter) throws InvalidOpenParachuteAltitude {
    super(_detachAltitude, _openParachuteAltitude);

    bmp280 = _bmp280;
    detachAltitudeParameter = _detachAltitudeParameter;
    openParachuteAltitudeParameter = _openParachuteAltitudeParameter;
    cicleTimeParameter = _cicleTimeParameter;

    /* Inizializzazione della servo board */
    servoBoard.setPWMFreq(60); // Set frequency to 60 Hz

    // create gpio controller
    GpioController gpio = GpioFactory.getInstance();
    detach  =         gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "detach", PinState.LOW);
    deployParachute = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "deploy", PinState.LOW);
    detach.setShutdownOptions(true, PinState.LOW);
    deployParachute.setShutdownOptions(true, PinState.LOW);

    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
    myButton.setShutdownOptions(true);
    // create and register gpio pin listener
    myButton.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        //System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
        if(event.getState() == PinState.LOW){
          reset();
        }
      }

    });

  }

  private void reset(){
    log.info("********* RESET *********");
    detach.low();
    deployParachute.low();
    deveAncoraScoppiare=true;
    ilParacaduteSiDeveAncoraAprire=true;
    int currentAltitude=0;
    try {
      currentAltitude = (int) bmp280.getAltitudeInMeter();
      detachAltitude = currentAltitude + detachAltitudeParameter;
      openParachuteAltitude = currentAltitude + openParachuteAltitudeParameter;
      deveAncoraScoppiare = true;
      ilParacaduteSiDeveAncoraAprire = true;
    } catch (Exception e) {
      e.printStackTrace();
    }

    log.info("Parametri al RESET");
    log.info("Start Altitude: " + currentAltitude + "m. DETACH at: " + detachAltitude + "m. OpenParachute at: " + openParachuteAltitude + "m. CicleTime: " + cicleTimeParameter + " milliseconds");



  }

  public void sganciaSonda() {
    //sgancia
    //Accendiamo il primo led
    log.info("********* DETACH *********");
    detach.high();
    servoBoard.inizializzaServoPerIlDrop(servoBoard,DROP_SERVO_CHANNEL,servoMin,servoMax);
    servoBoard.releasePayLoad(servoBoard,DROP_SERVO_CHANNEL,servoMin,servoMax);
  }

  public void apriParacadute() {
    //apri
    //Accendiamo il secondo led
    log.info("********* OPEN PARACHUTE *********");
    deployParachute.high();
  }
}
