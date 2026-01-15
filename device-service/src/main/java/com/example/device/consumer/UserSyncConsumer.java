package com.example.device.consumer;

import com.example.device.config.RabbitMQConfig;
import com.example.device.model.UserSync;
import com.example.device.model.SyncEvent;
import com.example.device.repo.UserSyncRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserSyncConsumer {

    private final UserSyncRepository userSyncRepository;

    public UserSyncConsumer(UserSyncRepository userSyncRepository) {
        this.userSyncRepository = userSyncRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.SYNC_QUEUE)
    public void handleSyncEvent(SyncEvent event) {
        if (event == null || event.getEntityType() == null) {
            return;
        }
        if (!"USER".equalsIgnoreCase(event.getEntityType())) {

            return;
        }
        System.out.println("Received user sync event: " + event);

        switch (event.getEventType()) {
            case "CREATED":
                UserSync userSync = new UserSync();
                userSync.setUserId(event.getUserId());
                userSync.setUsername(event.getUsername());
                userSync.setEmail(event.getEmail());
                userSyncRepository.save(userSync);
                System.out.println("User synced to device-service: " + event.getUserId());
                break;

            case "UPDATED":
                userSyncRepository.findById(event.getUserId()).ifPresent(user -> {
                    user.setUsername(event.getUsername());
                    user.setEmail(event.getEmail());
                    userSyncRepository.save(user);
                    System.out.println("User updated in device-service: " + event.getUserId());
                });
                break;

            case "DELETED":
                userSyncRepository.deleteById(event.getUserId());
                System.out.println("User deleted from device-service: " + event.getUserId());
                break;
            default:
                break;
        }
    }
}