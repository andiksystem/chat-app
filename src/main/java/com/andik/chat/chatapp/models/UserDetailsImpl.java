package com.andik.chat.chatapp.models;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.andik.chat.chatapp.entities.Pengguna;

public class UserDetailsImpl implements UserDetails {

    private final Pengguna pengguna;

    public UserDetailsImpl(Pengguna pengguna){
        this.pengguna = pengguna;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(pengguna.getAuthority()));
    }

    @Override
    public String getPassword() {
        return pengguna.getPassword();
    }

    @Override
    public String getUsername() {
        return pengguna.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}