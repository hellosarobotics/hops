package com.sarobotics.hops;

import com.sarobotics.actions.Action;
import com.sarobotics.actions.ActionHW;
import com.sarobotics.actions.ActionSimulator;
import com.sarobotics.bmp280.BMP280HW;
import com.sarobotics.bmp280.BMP280;
import com.sarobotics.bmp280.BMP280Simulator;
import com.sarobotics.utils.InvalidOpenParachuteAltitude;
import org.apache.log4j.Logger;

public class Main {



  public static void main(String... args) throws Exception, InvalidOpenParachuteAltitude {

    Logger log = Logger.getLogger(Main.class);

    if (args.length >= 2) {
      BMP280 bmp280;
      Action action;
      int actualAltitude, detachAltitude, openParachuteAltitude ;

      if ( args[0].equalsIgnoreCase("sim") ) {
        bmp280 = new BMP280Simulator();
        actualAltitude = (int) bmp280.getAltitudeInMeter();
        detachAltitude = actualAltitude +  Integer.parseInt(args[1]);
        openParachuteAltitude = actualAltitude + Integer.parseInt(args[2]);
        action = new ActionSimulator( detachAltitude, openParachuteAltitude );
        //action = new ActionHW(detachAltitude, openParachuteAltitude);
      } else {
        bmp280 = new BMP280HW();
        actualAltitude = (int) bmp280.getAltitudeInMeter();
        detachAltitude = actualAltitude +  Integer.parseInt(args[0]);
        openParachuteAltitude = actualAltitude + Integer.parseInt(args[1]);
        action = new ActionHW( detachAltitude, openParachuteAltitude );
      }

      log.info("Parametri allo start");
      log.info("Start Altitude: " +actualAltitude +"m. DETACH at: " + detachAltitude + "m. OpenParachute at: " + openParachuteAltitude+"m." );


      final AltitudeController ac = new AltitudeController(actualAltitude);


      while (true) {
        int altit = (int) bmp280.getAltitudeInMeter();
        if (altit != 3733) { //Hardcoded controllo su errore altitudine. Forse da imputare al BMP280HW un po' vecchiotto.
          ac.settaAltitudineAttuale(altit);
          if (ac.isGoingUp()) {
            log.info("\t  UP\t\t" + ac.print());
            if (action.canDetach(ac.getActualAltitude()) && action.getDeveAncoraScoppiare()) {
              action.setDeveAncoraScoppiare(false);
              action.sganciaSonda();
            }
          } else if (ac.isGoingDown()) {
            log.info("\t DOWN\t\t" + ac.print());
            if (action.canOpenParachute(ac.getActualAltitude())) {
              action.setIlParacaduteSiDeveAncoraAprire(false);
              action.apriParacadute();
            }
          }
//          else {
//            //Qui il sistema è stazionario
//          }
          Thread.sleep(10);
        }
      }

    } else {
      log.info("Manca qualcosa: esempio di esecuzione \njava -jar hops.jar 100 70\njava -jar hops.jar sim 100 7\nOppure se abbiamo problemi con la libreria pi4j aggiungere il seguente parametro alla VM: java -Dpi4j.linking=dynamic -jar.....blablabla ");
      System.exit(0);
    }
  }
}
