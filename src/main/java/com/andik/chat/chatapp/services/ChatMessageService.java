package com.andik.chat.chatapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andik.chat.chatapp.entities.ChatMessage;
import com.andik.chat.chatapp.exceptions.ResourceNotFoundException;
import com.andik.chat.chatapp.models.MessageStatus;
import com.andik.chat.chatapp.repositories.ChatMessageRepository;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setId(UUID.randomUUID().toString());
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public Long countNewMessage(String senderId, String recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId,
                MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);
        List<ChatMessage> messages = chatId.map(cid -> chatMessageRepository.findByChatId(cid))
                .orElse(new ArrayList<>());

        if (!messages.isEmpty()) {
            chatMessageRepository.updateStatus(MessageStatus.DELIVERED, senderId, recipientId);
        }

        return messages;
    }

    public ChatMessage findById(String id) {
        return chatMessageRepository.findById(id)
                .map(cm -> {
                    cm.setStatus(MessageStatus.DELIVERED);
                    return chatMessageRepository.save(cm);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Pesan tidak ditemukan"));
    }

}
