package com.sarobotics.bmp280;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;

public class BMP280HW implements BMP280 {

    private final I2C device;
    private int dig_T1, dig_T2, dig_T3;
    private int dig_P1, dig_P2, dig_P3, dig_P4, dig_P5, dig_P6, dig_P7, dig_P8, dig_P9;
    private final double seaLevel_hPa;

    public BMP280HW(Context pi4j, double seaLevel_hPa) throws Exception {
        this.seaLevel_hPa = seaLevel_hPa;

        I2CConfig config = I2C.newConfigBuilder(pi4j)
                .id("BMP280")
                .name("BMP280 Sensor")
                .bus(1)
                .device(0x77)
                .build();

        this.device = pi4j.create(config);

        byte[] b1 = new byte[24];
        device.readRegister(0x88, b1);

        dig_T1 = (b1[0] & 0xFF) + ((b1[1] & 0xFF) << 8);
        dig_T2 = (b1[2] & 0xFF) + ((b1[3] & 0xFF) << 8); if (dig_T2 > 32767) dig_T2 -= 65536;
        dig_T3 = (b1[4] & 0xFF) + ((b1[5] & 0xFF) << 8); if (dig_T3 > 32767) dig_T3 -= 65536;

        dig_P1 = (b1[6] & 0xFF) + ((b1[7] & 0xFF) << 8);
        dig_P2 = (b1[8] & 0xFF) + ((b1[9] & 0xFF) << 8); if (dig_P2 > 32767) dig_P2 -= 65536;
        dig_P3 = (b1[10] & 0xFF) + ((b1[11] & 0xFF) << 8); if (dig_P3 > 32767) dig_P3 -= 65536;
        dig_P4 = (b1[12] & 0xFF) + ((b1[13] & 0xFF) << 8); if (dig_P4 > 32767) dig_P4 -= 65536;
        dig_P5 = (b1[14] & 0xFF) + ((b1[15] & 0xFF) << 8); if (dig_P5 > 32767) dig_P5 -= 65536;
        dig_P6 = (b1[16] & 0xFF) + ((b1[17] & 0xFF) << 8); if (dig_P6 > 32767) dig_P6 -= 65536;
        dig_P7 = (b1[18] & 0xFF) + ((b1[19] & 0xFF) << 8); if (dig_P7 > 32767) dig_P7 -= 65536;
        dig_P8 = (b1[20] & 0xFF) + ((b1[21] & 0xFF) << 8); if (dig_P8 > 32767) dig_P8 -= 65536;
        dig_P9 = (b1[22] & 0xFF) + ((b1[23] & 0xFF) << 8); if (dig_P9 > 32767) dig_P9 -= 65536;

        device.writeRegister(0xF4, (byte) 0x27);
        device.writeRegister(0xF5, (byte) 0xA0);
    }

    public double getAltitudeInMeter() throws Exception {
        byte[] data = new byte[8];
        device.readRegister(0xF7, data);

        long adc_p = (((long) (data[0] & 0xFF) << 12) + ((long) (data[1] & 0xFF) << 4) + ((long) (data[2] & 0xF0) >> 4));
        long adc_t = (((long) (data[3] & 0xFF) << 12) + ((long) (data[4] & 0xFF) << 4) + ((long) (data[5] & 0xF0) >> 4));

        double var1 = (((double) adc_t) / 16384.0 - ((double) dig_T1) / 1024.0) * ((double) dig_T2);
        double var2 = ((((double) adc_t) / 131072.0 - ((double) dig_T1) / 8192.0) * (((double) adc_t) / 131072.0 - ((double) dig_T1) / 8192.0)) * ((double) dig_T3);
        double t_fine = var1 + var2;
        double temperature = t_fine / 5120.0;

        var1 = (t_fine / 2.0) - 64000.0;
        var2 = var1 * var1 * ((double) dig_P6) / 32768.0;
        var2 = var2 + var1 * ((double) dig_P5) * 2.0;
        var2 = (var2 / 4.0) + (((double) dig_P4) * 65536.0);
        var1 = (((double) dig_P3) * var1 * var1 / 524288.0 + ((double) dig_P2) * var1) / 524288.0;
        var1 = (1.0 + var1 / 32768.0) * ((double) dig_P1);
        double pressure = 1048576.0 - adc_p;
        pressure = (pressure - (var2 / 4096.0)) * 6250.0 / var1;
        var1 = ((double) dig_P9) * pressure * pressure / 2147483648.0;
        var2 = pressure * ((double) dig_P8) / 32768.0;
        pressure = (pressure + (var1 + var2 + ((double) dig_P7)) / 16.0) / 100;

        return fromHpaToMeter(pressure);
    }

    private double fromHpaToMeter(double hpa) {
        return 44330.0 * (1.0 - Math.pow(hpa / this.seaLevel_hPa, 0.1903));
    }
}
