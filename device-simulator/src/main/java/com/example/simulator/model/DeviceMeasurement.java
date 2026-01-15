package com.example.simulator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class DeviceMeasurement {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private Long deviceId;
    private Double measurementValue;

    public DeviceMeasurement() {}

    public DeviceMeasurement(LocalDateTime timestamp, Long deviceId, Double measurementValue) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

    public Double getMeasurementValue() { return measurementValue; }
    public void setMeasurementValue(Double measurementValue) { this.measurementValue = measurementValue; }

    @Override
    public String toString() {
        return "DeviceMeasurement{" +
                "timestamp=" + timestamp +
                ", deviceId=" + deviceId +
                ", measurementValue=" + measurementValue +
                '}';
    }
}