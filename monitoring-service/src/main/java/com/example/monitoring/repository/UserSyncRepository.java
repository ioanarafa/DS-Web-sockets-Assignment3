package com.example.monitoring.repository;

import com.example.monitoring.model.UserSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSyncRepository extends JpaRepository<UserSync, Long> {
}
