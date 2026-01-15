package com.example.user.publisher;

import com.example.user.config.RabbitMQConfig;
import com.example.user.model.SyncEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserSyncPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserSyncPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void publishUserCreated(Long userId, String username, String email) {
        SyncEvent event = new SyncEvent();
        event.setEventType("USER_CREATED");
        event.setUserId(userId);
        event.setUsername(username);
        event.setEmail(email);

        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, event);
        System.out.println("Published USER_CREATED event: " + event);
    }

    public void publishUserUpdated(Long userId, String username, String email) {
        SyncEvent event = new SyncEvent();
        event.setEventType("USER_UPDATED");
        event.setUserId(userId);
        event.setUsername(username);
        event.setEmail(email);

        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, event);
        System.out.println("Published USER_UPDATED event: " + event);
    }

    public void publishUserDeleted(Long userId) {
        SyncEvent event = new SyncEvent();
        event.setEventType("USER_DELETED");
        event.setUserId(userId);

        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, event);
        System.out.println("Published USER_DELETED event: " + event);
    }


    public void publishUserCreated(SyncEvent event) {
        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, event);
    }

    public void publishUserUpdated(SyncEvent event) {
        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, event);
    }

    public void publishUserDeleted(SyncEvent event) {
        rabbitTemplate.convertAndSend("", RabbitMQConfig.SYNC_QUEUE, event);
    }
}
