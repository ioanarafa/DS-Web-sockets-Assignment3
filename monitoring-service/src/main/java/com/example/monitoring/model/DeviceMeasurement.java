package com.example.monitoring.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceMeasurement {
    private LocalDateTime timestamp;
    private Long deviceId;
    private Double measurementValue;
}