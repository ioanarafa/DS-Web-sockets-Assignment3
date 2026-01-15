package com.example.simulator.generator;

import java.time.LocalDateTime;
import java.util.Random;

public class MeasurementGenerator {

    private final Random random = new Random();
    private double baseLoad;

    public MeasurementGenerator() {
        this.baseLoad = 1.5 + random.nextDouble() * 1.5;
    }

    public double generateMeasurement() {
        int hour = LocalDateTime.now().getHour();
        double multiplier;

        if (hour >= 0 && hour < 6) {
            multiplier = 0.5;
        } else if (hour >= 6 && hour < 9) {
            multiplier = 1.2;
        } else if (hour >= 9 && hour < 18) {
            multiplier = 1.0;
        } else if (hour >= 18 && hour < 23) {
            multiplier = 1.5;
        } else {
            multiplier = 0.8;
        }


        double noise = (random.nextDouble() - 0.5) * 0.2;

        double measurement = baseLoad * multiplier * (1 + noise);

        return Math.round(Math.max(0.1, measurement) * 100.0) / 100.0;
    }
}