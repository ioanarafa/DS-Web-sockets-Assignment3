package com.example.monitoring.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OverconsumptionAlert {
    private Long deviceId;
    private String deviceName;
    private Double currentConsumption;
    private Double maxConsumption;
    private LocalDateTime timestamp;
    private String message;

    public OverconsumptionAlert() {}

    public OverconsumptionAlert(Long deviceId, String deviceName, Double currentConsumption, Double maxConsumption, LocalDateTime timestamp) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.currentConsumption = currentConsumption;
        this.maxConsumption = maxConsumption;
        this.timestamp = timestamp;
        this.message = String.format("Device %s (ID: %d) exceeded maximum consumption: %.2f kWh / %.2f kWh", 
                deviceName, deviceId, currentConsumption, maxConsumption);
    }
}
