package com.example.monitoring.repository;

import com.example.monitoring.model.HourlyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, Long> {
    Optional<HourlyConsumption> findByDeviceIdAndHourTimestamp(Long deviceId, LocalDateTime hourTimestamp);
}