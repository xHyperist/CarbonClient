package com.carbonclient.account;

public final class AccountProfile {

    private final String userId;
    private final String displayName;

    public AccountProfile(String userId, String displayName) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User id cannot be empty.");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be empty.");
        }

        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
