package com.sarobotics.utils;

public class FromHpaToMeter {

  public static double fromHpaToMeter(double hpa, double tempF){

    double pa = hpa * 100;
    double div1 = pa/101325;
    double ln = Math.log(div1);
    double pr1 = ln * 287.053;
    //6.6507 x [(37 + 459.67) x 5/9]
    double pr2 = pr1 * ( (tempF+459.67) *(5d/9d));
    double div2 = pr2 / -9.8;

    return div2;
  }
}
