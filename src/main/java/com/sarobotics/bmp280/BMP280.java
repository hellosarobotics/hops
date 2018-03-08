package com.sarobotics.bmp280;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.sarobotics.utils.FromHpaToMeter;

import java.io.IOException;

public class BMP280 implements BMP280Interface{

  private I2CDevice device;
  private int dig_T1,dig_T2,dig_T3;
  private int dig_P1,dig_P2,dig_P3,dig_P4,dig_P5,dig_P6,dig_P7,dig_P8,dig_P9;

  public BMP280() throws Exception {

    // Create I2C bus
    I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
    // Get I2C device, BMP280 I2C address is 0x76(108)
    device = bus.getDevice(0x77);

    // Read 24 bytes of data from address 0x88(136)
    byte[] b1 = new byte[24];
    device.read(0x88, b1, 0, 24);

    // Convert the data
    // temp coefficents
    dig_T1 = (b1[0] & 0xFF) + ((b1[1] & 0xFF) * 256);
    dig_T2 = (b1[2] & 0xFF) + ((b1[3] & 0xFF) * 256);
    if(dig_T2 > 32767)
    {
      dig_T2 -= 65536;
    }
    dig_T3 = (b1[4] & 0xFF) + ((b1[5] & 0xFF) * 256);
    if(dig_T3 > 32767)
    {
      dig_T3 -= 65536;
    }

    // pressure coefficents
    dig_P1 = (b1[6] & 0xFF) + ((b1[7] & 0xFF) * 256);
    dig_P2 = (b1[8] & 0xFF) + ((b1[9] & 0xFF) * 256);
    if(dig_P2 > 32767)
    {
      dig_P2 -= 65536;
    }
     dig_P3 = (b1[10] & 0xFF) + ((b1[11] & 0xFF) * 256);
    if(dig_P3 > 32767)
    {
      dig_P3 -= 65536;
    }
    dig_P4 = (b1[12] & 0xFF) + ((b1[13] & 0xFF) * 256);
    if(dig_P4 > 32767)
    {
      dig_P4 -= 65536;
    }
    dig_P5 = (b1[14] & 0xFF) + ((b1[15] & 0xFF) * 256);
    if(dig_P5 > 32767)
    {
      dig_P5 -= 65536;
    }
    dig_P6 = (b1[16] & 0xFF) + ((b1[17] & 0xFF) * 256);
    if(dig_P6 > 32767)
    {
      dig_P6 -= 65536;
    }
    dig_P7 = (b1[18] & 0xFF) + ((b1[19] & 0xFF) * 256);
    if(dig_P7 > 32767)
    {
      dig_P7 -= 65536;
    }
    dig_P8 = (b1[20] & 0xFF) + ((b1[21] & 0xFF) * 256);
    if(dig_P8 > 32767)
    {
      dig_P8 -= 65536;
    }
    dig_P9 = (b1[22] & 0xFF) + ((b1[23] & 0xFF) * 256);
    if(dig_P9 > 32767)
    {
      dig_P9 -= 65536;
    }
    // Select control measurement register
    // Normal mode, temp and pressure over sampling rate = 1
    device.write(0xF4 , (byte)0x27);
    // Select config register
    // Stand_by time = 1000 ms
    device.write(0xF5 , (byte)0xA0);

  }

  public I2CDevice getDevice() {
    return device;
  }

  public void setDevice(I2CDevice device) {
    this.device = device;
  }

  public int getDig_T1() {
    return dig_T1;
  }

  public void setDig_T1(int dig_T1) {
    this.dig_T1 = dig_T1;
  }

  public int getDig_T2() {
    return dig_T2;
  }

  public void setDig_T2(int dig_T2) {
    this.dig_T2 = dig_T2;
  }

  public int getDig_T3() {
    return dig_T3;
  }

  public void setDig_T3(int dig_T3) {
    this.dig_T3 = dig_T3;
  }

  public int getDig_P1() {
    return dig_P1;
  }

  public void setDig_P1(int dig_P1) {
    this.dig_P1 = dig_P1;
  }

  public int getDig_P2() {
    return dig_P2;
  }

  public void setDig_P2(int dig_P2) {
    this.dig_P2 = dig_P2;
  }

  public int getDig_P3() {
    return dig_P3;
  }

  public void setDig_P3(int dig_P3) {
    this.dig_P3 = dig_P3;
  }

  public int getDig_P4() {
    return dig_P4;
  }

  public void setDig_P4(int dig_P4) {
    this.dig_P4 = dig_P4;
  }

  public int getDig_P5() {
    return dig_P5;
  }

  public void setDig_P5(int dig_P5) {
    this.dig_P5 = dig_P5;
  }

  public int getDig_P6() {
    return dig_P6;
  }

  public void setDig_P6(int dig_P6) {
    this.dig_P6 = dig_P6;
  }

  public int getDig_P7() {
    return dig_P7;
  }

  public void setDig_P7(int dig_P7) {
    this.dig_P7 = dig_P7;
  }

  public int getDig_P8() {
    return dig_P8;
  }

  public void setDig_P8(int dig_P8) {
    this.dig_P8 = dig_P8;
  }

  public int getDig_P9() {
    return dig_P9;
  }

  public void setDig_P9(int dig_P9) {
    this.dig_P9 = dig_P9;
  }

  public void leggiDalBMP280() throws IOException {
    // Read 8 bytes of data from address 0xF7(247)
    // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb, humidity lsb, humidity msb
    byte[] data = new byte[8];
    this.getDevice().read(0xF7, data, 0, 8);

    // Convert pressure and temperature data to 19-bits
    long adc_p = (((long) (data[0] & 0xFF) * 65536) + ((long) (data[1] & 0xFF) * 256) + (long) (data[2] & 0xF0)) / 16;
    long adc_t = (((long) (data[3] & 0xFF) * 65536) + ((long) (data[4] & 0xFF) * 256) + (long) (data[5] & 0xF0)) / 16;

    // Temperature offset calculations
    double var1 = (((double) adc_t) / 16384.0 - ((double) this.getDig_T1()) / 1024.0) * ((double) this.getDig_T2());
    double var2 = ((((double) adc_t) / 131072.0 - ((double) this.getDig_T1()) / 8192.0) *
            (((double) adc_t) / 131072.0 - ((double) this.getDig_T1()) / 8192.0)) * ((double) this.getDig_T3());
    double t_fine = (long) (var1 + var2);
    double cTemp = (var1 + var2) / 5120.0;
    double fTemp = cTemp * 1.8 + 32;

    // Pressure offset calculations
    var1 = ((double) t_fine / 2.0) - 64000.0;
    var2 = var1 * var1 * ((double) this.getDig_P6()) / 32768.0;
    var2 = var2 + var1 * ((double) this.getDig_P5()) * 2.0;
    var2 = (var2 / 4.0) + (((double) this.getDig_P4()) * 65536.0);
    var1 = (((double) this.getDig_P3()) * var1 * var1 / 524288.0 + ((double) this.getDig_P2()) * var1) / 524288.0;
    var1 = (1.0 + var1 / 32768.0) * ((double) this.getDig_P1());
    double p = 1048576.0 - (double) adc_p;
    p = (p - (var2 / 4096.0)) * 6250.0 / var1;
    var1 = ((double) this.getDig_P9()) * p * p / 2147483648.0;
    var2 = p * ((double) this.getDig_P8()) / 32768.0;
    double pressure = (p + (var1 + var2 + ((double) this.getDig_P7())) / 16.0) / 100;

//      // Output data to screen
//      System.out.printf("Pressure : %.2f hPa %n", pressure);
//      System.out.printf("Temperature in Celsius : %.2f C %n", cTemp);
//      System.out.printf("Temperature in Fahrenheit : %.2f F %n", fTemp);

    System.out.println( pressure + "   -   " + cTemp + " - " + fTemp);

  }


  public double getAltitudeInMeter() throws Exception {
    // Read 8 bytes of data from address 0xF7(247)
    // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb, humidity lsb, humidity msb
    byte[] data = new byte[8];
    this.getDevice().read(0xF7, data, 0, 8);

    // Convert pressure and temperature data to 19-bits
    long adc_p = (((long) (data[0] & 0xFF) * 65536) + ((long) (data[1] & 0xFF) * 256) + (long) (data[2] & 0xF0)) / 16;
    long adc_t = (((long) (data[3] & 0xFF) * 65536) + ((long) (data[4] & 0xFF) * 256) + (long) (data[5] & 0xF0)) / 16;

    // Temperature offset calculations
    double var1 = (((double) adc_t) / 16384.0 - ((double) this.getDig_T1()) / 1024.0) * ((double) this.getDig_T2());
    double var2 = ((((double) adc_t) / 131072.0 - ((double) this.getDig_T1()) / 8192.0) *
            (((double) adc_t) / 131072.0 - ((double) this.getDig_T1()) / 8192.0)) * ((double) this.getDig_T3());
    double t_fine = (long) (var1 + var2);
    double cTemp = (var1 + var2) / 5120.0;
    double fTemp = cTemp * 1.8 + 32;

    // Pressure offset calculations
    var1 = ((double) t_fine / 2.0) - 64000.0;
    var2 = var1 * var1 * ((double) this.getDig_P6()) / 32768.0;
    var2 = var2 + var1 * ((double) this.getDig_P5()) * 2.0;
    var2 = (var2 / 4.0) + (((double) this.getDig_P4()) * 65536.0);
    var1 = (((double) this.getDig_P3()) * var1 * var1 / 524288.0 + ((double) this.getDig_P2()) * var1) / 524288.0;
    var1 = (1.0 + var1 / 32768.0) * ((double) this.getDig_P1());
    double p = 1048576.0 - (double) adc_p;
    p = (p - (var2 / 4096.0)) * 6250.0 / var1;
    var1 = ((double) this.getDig_P9()) * p * p / 2147483648.0;
    var2 = p * ((double) this.getDig_P8()) / 32768.0;
    double pressure = (p + (var1 + var2 + ((double) this.getDig_P7())) / 16.0) / 100;

//      // Output data to screen
//      System.out.printf("Pressure : %.2f hPa %n", pressure);
//      System.out.printf("Temperature in Celsius : %.2f C %n", cTemp);
//      System.out.printf("Temperature in Fahrenheit : %.2f F %n", fTemp);

    double altitude = FromHpaToMeter.fromHpaToMeter(pressure,fTemp);
    //System.out.println( pressure + "   -   " + cTemp + " - " + fTemp + " - " + altitude);
    return altitude;

  }
}
