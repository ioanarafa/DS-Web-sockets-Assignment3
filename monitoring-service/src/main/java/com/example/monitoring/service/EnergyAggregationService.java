package com.example.monitoring.service;

import com.example.monitoring.model.DeviceMeasurement;
import com.example.monitoring.model.HourlyConsumption;
import com.example.monitoring.repository.HourlyConsumptionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class EnergyAggregationService {

    private final HourlyConsumptionRepository repository;

    public EnergyAggregationService(HourlyConsumptionRepository repository) {
        this.repository = repository;
    }

    public void processMeasurement(DeviceMeasurement measurement) {

        LocalDateTime hourTimestamp = measurement.getTimestamp().truncatedTo(ChronoUnit.HOURS);

        Optional<HourlyConsumption> existing = repository.findByDeviceIdAndHourTimestamp(
                measurement.getDeviceId(),
                hourTimestamp
        );

        if (existing.isPresent()) {

            HourlyConsumption record = existing.get();
            record.setTotalConsumption(record.getTotalConsumption() + measurement.getMeasurementValue());
            repository.save(record);
            System.out.println("Updated hourly consumption for device " + measurement.getDeviceId()
                    + " at " + hourTimestamp + ": " + record.getTotalConsumption() + " kWh");
        } else {

            HourlyConsumption record = new HourlyConsumption();
            record.setDeviceId(measurement.getDeviceId());
            record.setHourTimestamp(hourTimestamp);
            record.setTotalConsumption(measurement.getMeasurementValue());
            repository.save(record);
            System.out.println("Created new hourly consumption for device " + measurement.getDeviceId()
                    + " at " + hourTimestamp + ": " + record.getTotalConsumption() + " kWh");
        }
    }
}