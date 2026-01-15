package com.example.monitoring.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"deviceId", "hourTimestamp"}))
public class HourlyConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long deviceId;
    private LocalDateTime hourTimestamp;
    private Double totalConsumption;
}