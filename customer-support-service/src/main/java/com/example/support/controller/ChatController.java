package com.example.support.controller;

import com.example.support.config.RabbitMQConfig;
import com.example.support.model.ChatMessage;
import com.example.support.service.RuleBasedChatbotService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final RuleBasedChatbotService chatbotService;
    private final RabbitTemplate rabbitTemplate;

    public ChatController(RuleBasedChatbotService chatbotService, RabbitTemplate rabbitTemplate) {
        this.chatbotService = chatbotService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        message.setType("USER");

        rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_MESSAGES_QUEUE, message);

        String botResponse = chatbotService.processMessage(message.getContent(), message.getFrom());

        ChatMessage botMessage = new ChatMessage();
        botMessage.setFrom("Support Bot");
        botMessage.setTo(message.getFrom());
        botMessage.setFromUserId(0L);
        botMessage.setToUserId(message.getFromUserId());
        botMessage.setContent(botResponse);
        botMessage.setTimestamp(LocalDateTime.now());
        botMessage.setType("BOT");
        botMessage.setConversationId(message.getConversationId());

        rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_MESSAGES_QUEUE, botMessage);

        return ResponseEntity.ok(botMessage);
    }

    @PostMapping("/send-admin")
    public ResponseEntity<ChatMessage> sendAdminMessage(@RequestBody ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        message.setType("ADMIN");

        rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_MESSAGES_QUEUE, message);

        return ResponseEntity.ok(message);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Customer Support Service is running");
    }
}
