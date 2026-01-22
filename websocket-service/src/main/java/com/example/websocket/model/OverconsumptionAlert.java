package com.example.websocket.model;

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
}
