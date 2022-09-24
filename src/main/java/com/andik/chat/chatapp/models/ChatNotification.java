package com.andik.chat.chatapp.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotification implements Serializable {
    private String id;
    private String senderId;
    private String senderName;
}
