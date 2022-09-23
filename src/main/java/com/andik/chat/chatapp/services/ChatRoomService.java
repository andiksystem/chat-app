package com.andik.chat.chatapp.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andik.chat.chatapp.entities.ChatRoom;
import com.andik.chat.chatapp.repositories.ChatRoomRepository;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map((ChatRoom::getChatId))
                .or(() -> {
                    if (!createIfNotExist) {
                        return Optional.empty();
                    }

                    String chatId = String.format("%s_%s", senderId, recipientId);
                    ChatRoom senderRecipient = new ChatRoom();
                    senderRecipient.setId(UUID.randomUUID().toString());
                    senderRecipient.setChatId(chatId);
                    senderRecipient.setSenderId(senderId);
                    senderRecipient.setRecipientId(recipientId);

                    ChatRoom recipientSender = new ChatRoom();
                    recipientSender.setId(UUID.randomUUID().toString());
                    recipientSender.setChatId(chatId);
                    recipientSender.setSenderId(recipientId);
                    recipientSender.setRecipientId(senderId);

                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
