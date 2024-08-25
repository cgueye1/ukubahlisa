package com.wakana.samater.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.wakana.samater.chatroom.ChatRoomService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Date;
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
         // Obtention de l'heure actuelle de Dakar
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
        Date date = Date.from(now.toInstant());
      
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        chatMessage.setTimestamp(  date);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false).orElse(null);
        return repository.findByChatId(chatId);
        
        
        // chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
