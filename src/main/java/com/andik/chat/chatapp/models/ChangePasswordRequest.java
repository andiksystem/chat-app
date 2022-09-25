package com.andik.chat.chatapp.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChangePasswordRequest implements Serializable {
    private String username;
    private String newPassword;
    private String token;
}
