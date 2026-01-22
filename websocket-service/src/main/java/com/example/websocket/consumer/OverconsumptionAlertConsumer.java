package com.example.websocket.consumer;

import com.example.websocket.config.RabbitMQConfig;
import com.example.websocket.model.OverconsumptionAlert;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OverconsumptionAlertConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public OverconsumptionAlertConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.OVERCONSUMPTION_ALERTS_QUEUE)
    public void handleOverconsumptionAlert(OverconsumptionAlert alert) {
        System.out.println("Received overconsumption alert: " + alert);
        messagingTemplate.convertAndSend("/topic/overconsumption", alert);
    }
}
