package com.sarobotics.hops;

import com.sarobotics.bmp280.BMP280;
import com.sarobotics.bmp280.BMP280Interface;
import com.sarobotics.bmp280.BMP280Simulator;

public class Main {

  public static void main(String... args) throws Exception {



    BMP280Interface bmp280;

    if(args.length>0 && args[0].equalsIgnoreCase("sim")){
      bmp280  =  new BMP280Simulator();
    }else{
      bmp280  =  new BMP280();
    }


    int actualAltitude = (int) bmp280.getAltitudeInMeter();

    final AltitudeController ac = new AltitudeController(actualAltitude);
    SondeActions sa = new SondeActions(actualAltitude+100,actualAltitude+70);

    int counter = 1;

    while (true) {
      int altit = (int) bmp280.getAltitudeInMeter();
      if (altit!=3733) { //Hardcoded controllo su errore altitudine. Forse da imputare al BMP280 un po' vecchiotto.
        ac.settaAltitudineAttuale(altit);
        if (ac.isGoingUp()) {
          System.out.println(counter++ + "\t  UP\t\t" + ac.print());
          if (sa.canBurst(ac.getActualAltitude()) && sa.getDeveAncoraScoppiare()) {
            System.out.println("BURST POINT");
            sa.setDeveAncoraScoppiare(false);
          }
        } else if (ac.isGoingDown()) {
          System.out.println( counter++ + "\t DOWN\t\t" + ac.print());
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
  }
}
