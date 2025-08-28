package com.fotocapture.dms_backend.dto;

import java.util.List;

public class JwtResponse {
    private String token;
    private String refreshToken;
    private String username;
    private List<String> roles;

    // --- Constructors ---

    public JwtResponse(String token, String refreshToken, String username, List<String> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.roles = roles;
    }

    // --- Getters & Setters ---

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
