package com.example.simulator;

import com.example.simulator.generator.MeasurementGenerator;
import com.example.simulator.model.DeviceMeasurement;
import com.example.simulator.producer.RabbitMQProducer;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class SimulatorApplication {

    public static void main(String[] args) {
        Properties config = loadConfig();

        Long deviceId = Long.parseLong(config.getProperty("device.id"));
        String host = config.getProperty("rabbitmq.host");
        int port = Integer.parseInt(config.getProperty("rabbitmq.port"));
        String username = config.getProperty("rabbitmq.username");
        String password = config.getProperty("rabbitmq.password");
        String queue = config.getProperty("rabbitmq.queue");
        int intervalMinutes = Integer.parseInt(config.getProperty("interval.minutes", "10"));

        System.out.println("=================================");
        System.out.println("Device Simulator Starting...");
        System.out.println("Device ID: " + deviceId);
        System.out.println("RabbitMQ Host: " + host + ":" + port);
        System.out.println("Queue: " + queue);
        System.out.println("Interval: " + intervalMinutes + " minutes");
        System.out.println("=================================\n");

        RabbitMQProducer producer = new RabbitMQProducer(host, port, username, password, queue);
        MeasurementGenerator generator = new MeasurementGenerator();

        try {
            producer.connect();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("\nShutting down simulator...");
                    producer.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }));

            while (true) {
                LocalDateTime timestamp = LocalDateTime.now();
                double value = generator.generateMeasurement();

                DeviceMeasurement measurement = new DeviceMeasurement(timestamp, deviceId, value);
                producer.sendMeasurement(measurement);

                Thread.sleep(intervalMinutes * 60 * 1000L);
            }

        } catch (IOException | TimeoutException e) {
            System.err.println("Failed to connect to RabbitMQ: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Simulator interrupted");
            Thread.currentThread().interrupt();
        }
    }

    private static Properties loadConfig() {
        Properties config = new Properties();

        String deviceId = System.getenv("DEVICE_ID");
        String rabbitHost = System.getenv("RABBITMQ_HOST");
        String rabbitPort = System.getenv("RABBITMQ_PORT");
        String rabbitUser = System.getenv("RABBITMQ_USERNAME");
        String rabbitPass = System.getenv("RABBITMQ_PASSWORD");
        String intervalMin = System.getenv("INTERVAL_MINUTES");
        
        if (deviceId != null && rabbitHost != null) {
            config.setProperty("device.id", deviceId);
            config.setProperty("rabbitmq.host", rabbitHost);
            config.setProperty("rabbitmq.port", rabbitPort != null ? rabbitPort : "5672");
            config.setProperty("rabbitmq.username", rabbitUser != null ? rabbitUser : "admin");
            config.setProperty("rabbitmq.password", rabbitPass != null ? rabbitPass : "admin");
            config.setProperty("rabbitmq.queue", "device.measurements");
            config.setProperty("interval.minutes", intervalMin != null ? intervalMin : "10");
            return config;
        }
        

        try (FileInputStream fis = new FileInputStream("config.properties")) {
            config.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load config.properties: " + e.getMessage());
            System.err.println("Using default values...");
            config.setProperty("device.id", "1");
            config.setProperty("rabbitmq.host", "localhost");
            config.setProperty("rabbitmq.port", "5672");
            config.setProperty("rabbitmq.username", "admin");
            config.setProperty("rabbitmq.password", "admin");
            config.setProperty("rabbitmq.queue", "device.measurements");
            config.setProperty("interval.minutes", "10");
        }
        return config;
    }
}