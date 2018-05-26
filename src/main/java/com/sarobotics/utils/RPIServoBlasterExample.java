package com.sarobotics.utils;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.component.servo.impl.RPIServoBlasterProvider;

import java.util.Arrays;


public class RPIServoBlasterExample {

  public static void main(String[] args) throws Exception {


    String[] comando1 = {
            "echo", "P1-7=70 > /dev/servoblaster",

    };
    ExecuteShellCommand.executeCommand(comando1);

    Thread.sleep(1000);

    String[] comando2 = {
            "echo","P1-7=220 > /dev/servoblaster",

    };

    ExecuteShellCommand.executeCommand(comando2);

//    ServoProvider servoProvider = new RPIServoBlasterProvider();
//    ServoDriver servo7 = servoProvider.getServoDriver(servoProvider.getDefinedServoPins().get(7));
//    long start = System.currentTimeMillis();
//
//    while (System.currentTimeMillis() - start < 120000) { // 2 minutes
//      System.out.println("Up servo to 230");
//      for (int i = 65; i < 230; i++) {
//        servo7.setServoPulseWidth(i); // Set raw value for this servo driver - 50 to 195
//        Thread.sleep(10);
//      }
//      System.out.println("Down servo to 65");
//      for (int i = 230; i > 65; i--) {
//        servo7.setServoPulseWidth(i); // Set raw value for this servo driver - 50 to 195
//        Thread.sleep(10);
//      }
//    }
//    System.out.println("Exiting RPIServoBlasterExample");
  }
}