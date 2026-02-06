package com.example.support.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String from;
    private String to;
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private LocalDateTime timestamp;
    private String type;
    private String conversationId;
    
    public ChatMessage(String from, String to, String content, String type) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
}
