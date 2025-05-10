package com.sarobotics.utils;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class PCA9685 {

    private static final int PCA9685_ADDRESS = 0x40;
    private static final int MODE1 = 0x00;
    private static final int PRESCALE = 0xFE;
    private static final int LED0_ON_L = 0x06;
    private static final int LED0_ON_H = 0x07;
    private static final int LED0_OFF_L = 0x08;
    private static final int LED0_OFF_H = 0x09;

    private static boolean verbose = true;
    private int freq = 60;

    private I2C device;

    public PCA9685(Context pi4j) {
        try {
            I2CConfig config = I2C.newConfigBuilder(pi4j)
                    .id("PCA9685")
                    .name("PWM Driver")
                    .bus(1)
                    .device(PCA9685_ADDRESS)
                    .build();
            this.device = pi4j.create(config);

            if (verbose) System.out.println("Connected to PCA9685 device");
            device.writeRegister(MODE1, (byte) 0x00); // Reset
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize PCA9685", e);
        }
    }

    public void setPWMFreq(int freq) {
        this.freq = freq;
        float preScaleVal = 25_000_000.0f / 4096 / freq - 1.0f;
        int preScale = Math.round(preScaleVal);

        if (verbose) {
            System.out.println("Setting PWM frequency to " + freq + " Hz, prescale: " + preScale);
        }

        try {
            byte oldmode = (byte) device.readRegister(MODE1);
            byte newmode = (byte) ((oldmode & 0x7F) | 0x10); // Sleep
            device.writeRegister(MODE1, newmode);
            device.writeRegister(PRESCALE, (byte) preScale);
            device.writeRegister(MODE1, oldmode);
            sleep(5);
            device.writeRegister(MODE1, (byte) (oldmode | 0x80));
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to set PWM frequency", e);
        }
    }

    public void setPWM(int channel, int on, int off) {
        if (channel < 0 || channel > 15 || on < 0 || on > 4095 || off < 0 || off > 4095 || on > off) {
            throw new IllegalArgumentException("Invalid PWM parameters");
        }

        try {
            device.writeRegister(LED0_ON_L + 4 * channel, (byte) (on & 0xFF));
            device.writeRegister(LED0_ON_H + 4 * channel, (byte) (on >> 8));
            device.writeRegister(LED0_OFF_L + 4 * channel, (byte) (off & 0xFF));
            device.writeRegister(LED0_OFF_H + 4 * channel, (byte) (off >> 8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to set PWM", e);
        }
    }

    public void releasePayLoad(int channel, int servoMin, int servoMax) {
        long start = System.currentTimeMillis();
        for (int i = servoMax; i >= servoMin; i--) {
            setPWM(channel, 0, i);
        }
        System.out.println((System.currentTimeMillis() - start) + " Millisecondi");
    }

    public void inizializzaServoPerIlDrop(int channel, int servoMin, int servoMax) {
        for (int i = servoMin; i <= servoMax; i++) {
            setPWM(channel, 0, i);
        }
    }

    public void deployParachute(int channel, int servoPulse) {
        setPWM(channel, 0, servoPulse);
    }
}