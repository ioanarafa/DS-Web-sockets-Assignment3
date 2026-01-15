package com.example.device.repo;

import com.example.device.model.UserSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSyncRepository extends JpaRepository<UserSync, Long> {
}