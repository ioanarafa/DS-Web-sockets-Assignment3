package com.example.support.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String from;           // username
    private String to;             // username
    private Long fromUserId;       // user ID for routing
    private Long toUserId;         // user ID for routing
    private String content;
    private LocalDateTime timestamp;
    private String type;           // "USER", "ADMIN", "BOT"
    private String conversationId; // to group messages by conversation
    
    public ChatMessage(String from, String to, String content, String type) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
}
