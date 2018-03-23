package com.sarobotics.bmp280;

public class BMP280Simulator  implements BMP280 {

  private double currentAltitudeSimulated;

  public  BMP280Simulator( double seaLevel_hPa ) {
    currentAltitudeSimulated=0.0;
    new Thread(() -> {
      //boolean running = true;
      try {
        //Attendiamo 10 secondi prima di far partire la simulazione del bmp280 perché l'inizializzazione del main impiega qualche secondo.
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      //while(running) {
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
        //running=false;
        System.exit(0);
      //}
    }).start();
  }

  public double getAltitudeInMeter() {
    return currentAltitudeSimulated;
  }
}
