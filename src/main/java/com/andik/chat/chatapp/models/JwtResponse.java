package com.andik.chat.chatapp.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtResponse implements Serializable {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;

    public JwtResponse(
            String accessToken,
            String username,
            String email) {
        this.username = username;
        this.email = email;
        this.token = accessToken;
    }

    public JwtResponse(
            String accessToken,
            String username) {
        this.username = username;
        this.token = accessToken;
    }

}
