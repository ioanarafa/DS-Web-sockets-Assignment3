package com.example.monitoring.consumer;

import com.example.monitoring.config.RabbitMQConfig;
import com.example.monitoring.model.DeviceSync;
import com.example.monitoring.model.SyncEvent;
import com.example.monitoring.model.UserSync;
import com.example.monitoring.repository.DeviceSyncRepository;
import com.example.monitoring.repository.UserSyncRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SyncConsumer {

    private final DeviceSyncRepository deviceRepo;
    private final UserSyncRepository userRepo;

    public SyncConsumer(DeviceSyncRepository deviceRepo, UserSyncRepository userRepo) {
        this.deviceRepo = deviceRepo;
        this.userRepo = userRepo;
    }

    @RabbitListener(queues = RabbitMQConfig.SYNC_QUEUE)
    public void handleSyncEvent(SyncEvent event) {
        if (event == null || event.getEntityType() == null || event.getEventType() == null) {
            return;
        }

        String entity = event.getEntityType().toUpperCase();
        String type = event.getEventType().toUpperCase();

        switch (entity) {
            case "DEVICE" -> handleDevice(type, event);
            case "USER" -> handleUser(type, event);
            default -> {
                // ignore
            }
        }
    }

    private void handleDevice(String type, SyncEvent event) {
        if (event.getDeviceId() == null) return;

        switch (type) {
            case "CREATED" -> {
                DeviceSync d = new DeviceSync();
                d.setDeviceId(event.getDeviceId());
                d.setDeviceName(event.getName());
                d.setMaxConsumption(event.getMaxConsumption());
                deviceRepo.save(d);
            }
            case "UPDATED" -> deviceRepo.findById(event.getDeviceId()).ifPresent(d -> {
                d.setDeviceName(event.getName());
                d.setMaxConsumption(event.getMaxConsumption());
                deviceRepo.save(d);
            });
            case "DELETED" -> deviceRepo.deleteById(event.getDeviceId());
            default -> {
                // no-op
            }
        }
    }

    private void handleUser(String type, SyncEvent event) {
        if (event.getUserId() == null) return;

        switch (type) {
            case "CREATED" -> {
                UserSync u = new UserSync();
                u.setUserId(event.getUserId());
                u.setUsername(event.getUsername());
                u.setEmail(event.getEmail());
                userRepo.save(u);
            }
            case "UPDATED" -> userRepo.findById(event.getUserId()).ifPresent(u -> {
                u.setUsername(event.getUsername());
                u.setEmail(event.getEmail());
                userRepo.save(u);
            });
            case "DELETED" -> userRepo.deleteById(event.getUserId());
            default -> {
                // no-op
            }
        }
    }
}
