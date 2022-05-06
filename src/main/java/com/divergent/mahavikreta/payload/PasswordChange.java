package com.divergent.mahavikreta.payload;

import lombok.Data;

@Data
public class PasswordChange{

    private String currentPassword;
    private String newPassword;
    private String username;

    public PasswordChange(String currentPassword,String newPassword,String username){
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.username = username;
    }
    public PasswordChange(){
        super();
        // TODO Auto-generated constructor stub
    }
}

