package com.example.simulator.producer;

import com.example.simulator.model.DeviceMeasurement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQProducer {

    private final ConnectionFactory factory;
    private final String queueName;
    private final ObjectMapper objectMapper;
    private Connection connection;
    private Channel channel;

    public RabbitMQProducer(String host, int port, String username, String password, String queueName) {
        this.factory = new ConnectionFactory();
        this.factory.setHost(host);
        this.factory.setPort(port);
        this.factory.setUsername(username);
        this.factory.setPassword(password);
        this.queueName = queueName;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void connect() throws IOException, TimeoutException {
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);
        System.out.println("Connected to RabbitMQ on queue: " + queueName);
    }

    public void sendMeasurement(DeviceMeasurement measurement) throws IOException {
        String json = objectMapper.writeValueAsString(measurement);
        channel.basicPublish("", queueName, null, json.getBytes());
        System.out.println("Sent: " + measurement);
    }

    public void close() throws IOException, TimeoutException {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
        System.out.println("Disconnected from RabbitMQ");
    }
}