package com.example.monitoring.service;

import com.example.monitoring.config.RabbitMQConfig;
import com.example.monitoring.model.DeviceMeasurement;
import com.example.monitoring.model.DeviceSync;
import com.example.monitoring.model.HourlyConsumption;
import com.example.monitoring.model.OverconsumptionAlert;
import com.example.monitoring.repository.DeviceSyncRepository;
import com.example.monitoring.repository.HourlyConsumptionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class EnergyAggregationService {

    private final HourlyConsumptionRepository repository;
    private final DeviceSyncRepository deviceSyncRepository;
    private final RabbitTemplate rabbitTemplate;

    public EnergyAggregationService(HourlyConsumptionRepository repository, 
                                   DeviceSyncRepository deviceSyncRepository,
                                   RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.deviceSyncRepository = deviceSyncRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void processMeasurement(DeviceMeasurement measurement) {

        LocalDateTime hourTimestamp = measurement.getTimestamp().truncatedTo(ChronoUnit.HOURS);

        Optional<HourlyConsumption> existing = repository.findByDeviceIdAndHourTimestamp(
                measurement.getDeviceId(),
                hourTimestamp
        );

        HourlyConsumption record;
        if (existing.isPresent()) {
            record = existing.get();
            record.setTotalConsumption(record.getTotalConsumption() + measurement.getMeasurementValue());
            repository.save(record);
            System.out.println("Updated hourly consumption for device " + measurement.getDeviceId()
                    + " at " + hourTimestamp + ": " + record.getTotalConsumption() + " kWh");
        } else {
            record = new HourlyConsumption();
            record.setDeviceId(measurement.getDeviceId());
            record.setHourTimestamp(hourTimestamp);
            record.setTotalConsumption(measurement.getMeasurementValue());
            repository.save(record);
            System.out.println("Created new hourly consumption for device " + measurement.getDeviceId()
                    + " at " + hourTimestamp + ": " + record.getTotalConsumption() + " kWh");
        }

        checkAndAlertOverconsumption(record);
    }

    private void checkAndAlertOverconsumption(HourlyConsumption consumption) {
        Optional<DeviceSync> deviceOpt = deviceSyncRepository.findById(consumption.getDeviceId());
        if (deviceOpt.isPresent()) {
            DeviceSync device = deviceOpt.get();
            if (device.getMaxConsumption() != null && consumption.getTotalConsumption() > device.getMaxConsumption()) {
                OverconsumptionAlert alert = new OverconsumptionAlert(
                        consumption.getDeviceId(),
                        device.getDeviceName(),
                        consumption.getTotalConsumption(),
                        device.getMaxConsumption(),
                        consumption.getHourTimestamp()
                );
                rabbitTemplate.convertAndSend(RabbitMQConfig.OVERCONSUMPTION_ALERTS_QUEUE, alert);
                System.out.println("Overconsumption alert sent for device " + consumption.getDeviceId());
            }
        }
    }
}