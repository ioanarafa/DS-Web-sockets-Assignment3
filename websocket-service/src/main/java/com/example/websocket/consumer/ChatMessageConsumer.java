package com.example.websocket.consumer;

import com.example.websocket.config.RabbitMQConfig;
import com.example.websocket.model.ChatMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.CHAT_MESSAGES_QUEUE)
    public void handleChatMessage(ChatMessage message) {
        System.out.println("Received chat message from: " + message.getFrom() + " to: " + message.getTo() + " type: " + message.getType());
        
        if ("USER".equals(message.getType())) {
            System.out.println("Sending USER message to admin dashboard");
            messagingTemplate.convertAndSend("/topic/admin-messages", message);
            
        } else if ("ADMIN".equals(message.getType())) {
            System.out.println("Sending ADMIN message to client: " + message.getTo());
            if (message.getToUserId() != null) {
                messagingTemplate.convertAndSend("/topic/client-messages-" + message.getToUserId(), message);
            } else if (message.getTo() != null && !message.getTo().isEmpty()) {
                messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/messages", message);
            }
            
        } else if ("BOT".equals(message.getType())) {
            System.out.println("Sending BOT message to client: " + message.getTo());
            if (message.getToUserId() != null) {
                messagingTemplate.convertAndSend("/topic/client-messages-" + message.getToUserId(), message);
            } else if (message.getTo() != null && !message.getTo().isEmpty()) {
                messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/messages", message);
            }
        }
    }
}
