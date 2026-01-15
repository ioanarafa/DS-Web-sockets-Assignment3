package com.example.monitoring.repository;

import com.example.monitoring.model.DeviceSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceSyncRepository extends JpaRepository<DeviceSync, Long> {
}