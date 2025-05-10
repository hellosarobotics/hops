package com.sarobotics.hops;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.sarobotics.actions.Action;
import com.sarobotics.actions.ActionHW;
import com.sarobotics.actions.ActionSimulator;
import com.sarobotics.bmp280.BMP280HW;
import com.sarobotics.bmp280.BMP280;
import com.sarobotics.bmp280.BMP280Simulator;
import com.sarobotics.utils.InvalidOpenParachuteAltitude;
//import com.sarobotics.utils.PCA9685;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String... args) {
    try {
      if (args.length >= 4) {
    	  
    	  Context pi4j = Pi4J.newAutoContext();

        BMP280 bmp280;
        Action action;


        /*
         * actualAltitude: L'altitudine attuale al primo avvio del BMP280
         * detachAltitude: Altitudine di distacco della sonda
         * openParachuteAltitude: Altitudine di apertura paracadute
         * cicleTime: velocita' del ciclo di lettura dal BMP280
         * seaLevel_hPa: Pressione atmosferica al livello del mare.
         */
        int actualAltitude, detachAltitude, openParachuteAltitude, cicleTime;
        double seaLevel_hPa;
        boolean continua = true;

        int indiceArgomenti = 0;
        if (args[indiceArgomenti].equalsIgnoreCase("sim")) {
          seaLevel_hPa = Double.parseDouble(args[++indiceArgomenti]);
          bmp280 = new BMP280Simulator(seaLevel_hPa);
          actualAltitude = (int) bmp280.getAltitudeInMeter();
          detachAltitude = actualAltitude + Integer.parseInt(args[++indiceArgomenti]);
          openParachuteAltitude = actualAltitude + Integer.parseInt(args[++indiceArgomenti]);
          action = new ActionSimulator(detachAltitude, openParachuteAltitude);
          cicleTime = Integer.parseInt(args[++indiceArgomenti]);
        } else {
          seaLevel_hPa = Double.parseDouble(args[indiceArgomenti++]);
          bmp280 = new BMP280HW(pi4j, seaLevel_hPa );
          actualAltitude = (int) bmp280.getAltitudeInMeter();
          // Trick... spesso il bmp280 all'accensione da un errore troppo grande in termini di misura della pressione il seguente if e' un espediente per evitarlo. 
          // Ovviamente se allo start ci troviamo ad una altitudine > 1000 metri attendiamo comunque 5 secondi e riprendiamo l'altitudine per sicurezza. 
          if (actualAltitude > 1000) { //HARDCODED ma per i test va bene cosi'
            Thread.sleep(5000);
            actualAltitude = (int) bmp280.getAltitudeInMeter();
          }
          int detachAltitudeParameter = Integer.parseInt(args[indiceArgomenti++]);
          int openParachuteAltitudeParameter = Integer.parseInt(args[indiceArgomenti++]);
          int cicleTimeParameter = Integer.parseInt(args[indiceArgomenti]);
          detachAltitude = actualAltitude + detachAltitudeParameter;
          openParachuteAltitude = actualAltitude + openParachuteAltitudeParameter;
          cicleTime = cicleTimeParameter;
          action = new ActionHW(detachAltitude, openParachuteAltitude,bmp280,detachAltitudeParameter,openParachuteAltitudeParameter,cicleTimeParameter);
        }

        log.info("Parametri allo start");
        log.info("Start Altitude: " + actualAltitude + "m. DETACH at: " + detachAltitude + "m. OpenParachute at: " + openParachuteAltitude + "m. CicleTime: " + cicleTime + " milliseconds");

        final AltitudeController ac = new AltitudeController(actualAltitude);

          while (continua) {
            try {
                ac.settaAltitudineAttuale( (int) bmp280.getAltitudeInMeter() );
                if ( ac.isGoingUp() ) {
                  log.info("\t  UP\t\t" + ac.print());
                  if (action.canDetach(ac.getActualAltitude()) && action.getDeveAncoraScoppiare()) {
                    action.setDeveAncoraScoppiare(false);
                    action.sganciaSonda();
                  }
                } else if ( ac.isGoingDown() ) {
                  log.info("\t DOWN\t\t" + ac.print());
                  if (action.canOpenParachute(ac.getActualAltitude())) {
                    action.setIlParacaduteSiDeveAncoraAprire(false);
                    action.apriParacadute();
                  }
                }
              Thread.sleep(cicleTime);
            }catch (Exception e){
              continua = false;
              e.printStackTrace();
              log.error(e.getMessage());
            }
          }

      } else {
        log.info(messaggioParametri());
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("<-- PROBABILE ERRORE SUI PARAMETRI --> \n" + messaggioParametri());
      System.exit(0);
    } catch (InvalidOpenParachuteAltitude invalidOpenParachuteAltitude) {
      invalidOpenParachuteAltitude.printStackTrace();
      System.exit(0);
    }
  }

  private static String messaggioParametri() {
    return "I parametri da dare in pasto sono, pressione atmosferica al livello del mare di oggi: 1000.6 hPa; altitudine per il detach: (current + 100metri) 100; altitudine per aprire il paracadute: (current + 70 metri)70; tempo di ciclo in millisecondi: 1000 --- \nManca qualcosa: esempio di esecuzione \njava -jar hops.jar 100 70 1000\njava -jar hops.jar sim 100 70 1000\n\nOppure se abbiamo problemi con la libreria pi4j aggiungere il seguente parametro alla VM: java -Dpi4j.linking=dynamic -jar.....blablabla ";
  }
}
