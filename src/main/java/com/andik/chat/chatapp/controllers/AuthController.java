package com.andik.chat.chatapp.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andik.chat.chatapp.entities.Pengguna;
import com.andik.chat.chatapp.jwt.JwtUtils;
import com.andik.chat.chatapp.models.ChangePasswordRequest;
import com.andik.chat.chatapp.models.JwtResponse;
import com.andik.chat.chatapp.models.LoginRequest;
import com.andik.chat.chatapp.models.ResetPasswordRequest;
import com.andik.chat.chatapp.models.SignupRequest;
import com.andik.chat.chatapp.models.UserDetailsImpl;
import com.andik.chat.chatapp.services.PenggunaService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PenggunaService penggunaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok().body(new JwtResponse(token, principal.getUsername()));
    }

    @PostMapping("/signup")
    public Pengguna signup(@RequestBody SignupRequest request) {
        Pengguna pengguna = new Pengguna();
        pengguna.setUsername(request.getUsername());
        pengguna.setPassword(passwordEncoder.encode(request.getPassword()));
        pengguna.setAuthority("USER");

        pengguna.setEmail(request.getEmail());
        pengguna.setNamaLengkap(request.getNama());
        Pengguna created = penggunaService.create(pengguna);
        return created;
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        Pengguna pengguna = penggunaService.resetPassword(request.getEmail());
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Permintaan reset password terlah dikirim ke " + request.getEmail());
        map.put("resetPasswordToken", pengguna.getChangePasswordToken());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        penggunaService.changePassword(request.getUsername(), request.getToken(),
                passwordEncoder.encode(request.getNewPassword()));
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Password telah sukses diubah");
        return ResponseEntity.ok(map);
    }

}
