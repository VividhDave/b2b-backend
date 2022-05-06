package com.divergent.mahavikreta.entity.filter;

import lombok.Data;

@Data
public class PasswordChange {
    private String currentPassword;
    private String newPassword;
    private String username;

    @Override
    public String toString() {
        return "PasswordChange{" +
                "currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public PasswordChange() {
        super();
    }
}
