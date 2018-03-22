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

  public static void main(String... args) {
    Logger log = Logger.getLogger(Main.class);

    try {

      if (args.length >= 3) {
        BMP280 bmp280;
        Action action;
        int actualAltitude, detachAltitude, openParachuteAltitude, tempoCiclo;

        int indiceArgomenti = 0;
        if (args[indiceArgomenti].equalsIgnoreCase("sim")) {
          bmp280 = new BMP280Simulator();
          actualAltitude = (int) bmp280.getAltitudeInMeter();
          detachAltitude = actualAltitude + Integer.parseInt(args[++indiceArgomenti]);
          openParachuteAltitude = actualAltitude + Integer.parseInt(args[++indiceArgomenti]);
          action = new ActionSimulator(detachAltitude, openParachuteAltitude);
          tempoCiclo = Integer.parseInt(args[++indiceArgomenti]);
          //action = new ActionHW(detachAltitude, openParachuteAltitude);
        } else {
          bmp280 = new BMP280HW();
          actualAltitude = (int) bmp280.getAltitudeInMeter();
          detachAltitude = actualAltitude + Integer.parseInt(args[indiceArgomenti++]);
          openParachuteAltitude = actualAltitude + Integer.parseInt(args[indiceArgomenti++]);
          action = new ActionHW(detachAltitude, openParachuteAltitude);
          tempoCiclo = Integer.parseInt(args[indiceArgomenti++]);
        }

        log.info("Parametri allo start");
        log.info("Start Altitude: " + actualAltitude + "m. DETACH at: " + detachAltitude + "m. OpenParachute at: " + openParachuteAltitude + "m.");


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
            Thread.sleep(tempoCiclo);
          }
        }

      } else {
        log.info(messaggioParametri());
        //System.exit(0);
      }
    }catch (Exception e){
      e.printStackTrace();
      log.error("<-- PROBABILE ERRORE SUI PARAMETRI --> \n"+ messaggioParametri() );
      System.exit(0);
    } catch (InvalidOpenParachuteAltitude invalidOpenParachuteAltitude) {
      invalidOpenParachuteAltitude.printStackTrace();
      System.exit(0);
    }
  }

  private static Object messaggioParametri() {
    return "I parametri da dare in pasto sono, altitudine per il detach: 100; altitudine per aprire il paracadute:70; tempo di ciclo in millisecondi: 1000 --- \nManca qualcosa: esempio di esecuzione \njava -jar hops.jar 100 70 1000\njava -jar hops.jar sim 100 70 1000\n\nOppure se abbiamo problemi con la libreria pi4j aggiungere il seguente parametro alla VM: java -Dpi4j.linking=dynamic -jar.....blablabla ";
  }
}
