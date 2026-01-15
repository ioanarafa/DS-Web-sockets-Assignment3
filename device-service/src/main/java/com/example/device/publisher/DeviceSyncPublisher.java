package com.example.device.publisher;

import com.example.device.model.SyncEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeviceSyncPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.sync-queue:sync.events}")
    private String syncQueue;

    public DeviceSyncPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(SyncEvent event) {
        rabbitTemplate.convertAndSend(syncQueue, event);
    }
}
