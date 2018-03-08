package com.sarobotics.bmp280;

public class BMP280Simulator  implements BMP280Interface{

  double currentAltitudeSimulated;

  public  BMP280Simulator() {
    new Thread(){
      @Override
      public void run() {
        while(true) {
          for (int i = 0; i < 150; i++) {
            //ac.settaAltitudineAttuale(i);
            currentAltitudeSimulated = (double)i;
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          for (int i = 150; i > 0; i--) {
            //ac.settaAltitudineAttuale(i);
            currentAltitudeSimulated = (double)i;
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }.start();
  }

  public double getAltitudeInMeter() {
    return currentAltitudeSimulated;
  }
}
