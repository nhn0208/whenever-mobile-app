package com.example.wheneverapp.Model;

public class LoginResponse {
    private String message;
    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
