package com.sarobotics.hops;

import com.sarobotics.bmp280.BMP280HW;
import com.sarobotics.bmp280.BMP280;
import com.sarobotics.bmp280.BMP280Simulator;
import com.sarobotics.utils.InvalidOpenParachuteAltitude;

public class Main {

  public static void main(String... args) throws Exception, InvalidOpenParachuteAltitude {

    if (args.length >= 2) {
      BMP280 bmp280;
      int burstAltitude, openParachuteAltitude;

      if (args.length > 0 && args[0].equalsIgnoreCase("sim")) {
        bmp280 = new BMP280Simulator();
        burstAltitude = Integer.parseInt(args[1]);
        openParachuteAltitude = Integer.parseInt(args[2]);
      } else {
        bmp280 = new BMP280HW();
        burstAltitude = Integer.parseInt(args[0]);
        openParachuteAltitude = Integer.parseInt(args[1]);
      }

      int actualAltitude = (int) bmp280.getAltitudeInMeter();

      final AltitudeController ac = new AltitudeController(actualAltitude);
      SondeActions sa = new SondeActions(actualAltitude + burstAltitude, actualAltitude + openParachuteAltitude);

      int counter = 1;

      while (true) {
        int altit = (int) bmp280.getAltitudeInMeter();
        if (altit != 3733) { //Hardcoded controllo su errore altitudine. Forse da imputare al BMP280HW un po' vecchiotto.
          ac.settaAltitudineAttuale(altit);
          if (ac.isGoingUp()) {
            System.out.println(counter++ + "\t  UP\t\t" + ac.print());
            if (sa.canBurst(ac.getActualAltitude()) && sa.getDeveAncoraScoppiare()) {
              System.out.println("BURST POINT");
              sa.setDeveAncoraScoppiare(false);
            }
          } else if (ac.isGoingDown()) {
            System.out.println(counter++ + "\t DOWN\t\t" + ac.print());
            if (sa.canOpenParachute(ac.getActualAltitude())) {
              System.out.println("OPEN PARACHUTE");
              sa.setIlParacaduteSiDeveAncoraAprire(false);
            }
          } else {
            //Qui il sistema è stazionario
            //System.out.println( "STAZIONARIO: " + ac.print() );
          }
          Thread.sleep(10);
        }
      }

    } else {
      System.out.println("Inserire gli argomenti corretti: (sim 100 70) oppure solo (100 70)");
      System.exit(0);
    }
  }
}
