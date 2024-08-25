package com.wakana.samater.chatroom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }

                    return  Optional.of(String.format("%s_%s", senderId, recipientId));
                });
    }

    private String createChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = new ChatRoom();
        senderRecipient.setChatId(chatId);
        senderRecipient.setChatId(senderId);
        senderRecipient.setRecipientId(recipientId);
    
               /*  .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();*/

        ChatRoom recipientSender = new ChatRoom();
               /*  .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();*/
                
                recipientSender.setChatId(chatId);
                recipientSender.setSenderId(recipientId);
                recipientSender.setRecipientId(senderId);
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
