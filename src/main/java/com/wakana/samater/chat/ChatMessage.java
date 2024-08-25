package com.wakana.samater.chat;
import lombok.Data;
import jakarta.persistence.*;

import java.util.Date;

@Data
@Entity
@Table(name = "chatmessage")
public class ChatMessage {
   @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;
}
