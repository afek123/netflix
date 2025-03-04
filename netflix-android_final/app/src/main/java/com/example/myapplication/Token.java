package com.example.myapplication;

public class Token {

    private String token;
    private String userId;
    private String role;

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
