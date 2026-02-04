package com.example.superapp.feature.auth.data.model;

/**
 * Data class that captures user information for logged in users
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String email;

    public LoggedInUser(String userId, String displayName, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
