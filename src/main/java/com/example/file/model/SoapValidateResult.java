package com.example.file.model;

public class SoapValidateResult {
    private String userId;
    private String role;

    public SoapValidateResult(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getRole() { return role; }
}