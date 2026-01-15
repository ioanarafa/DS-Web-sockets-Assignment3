package com.example.monitoring.consumer;

import com.example.monitoring.config.RabbitMQConfig;
import com.example.monitoring.model.DeviceMeasurement;
import com.example.monitoring.service.EnergyAggregationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MeasurementConsumer {

    private final EnergyAggregationService aggregationService;

    public MeasurementConsumer(EnergyAggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @RabbitListener(queues = RabbitMQConfig.MEASUREMENTS_QUEUE)
    public void consumeMeasurement(DeviceMeasurement measurement) {
        System.out.println("Received measurement: " + measurement);
        aggregationService.processMeasurement(measurement);
    }
}