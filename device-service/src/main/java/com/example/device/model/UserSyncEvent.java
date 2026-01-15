package com.example.device.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSyncEvent {
    private Long userId;
    private String username;
    private String email;
    private String eventType;
}