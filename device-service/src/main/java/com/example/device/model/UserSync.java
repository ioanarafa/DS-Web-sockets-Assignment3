package com.example.device.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_sync")
public class UserSync {
    @Id
    private Long userId;
    private String username;
    private String email;
}