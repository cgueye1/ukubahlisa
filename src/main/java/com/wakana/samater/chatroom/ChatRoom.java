package com.wakana.samater.chatroom;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "chatroom")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
