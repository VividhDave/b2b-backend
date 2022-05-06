package com.divergent.mahavikreta.service;

import org.springframework.stereotype.Component;

@Component
public interface EmailUtility {

    public void sendMail(String toEmail,String subject,String newPassword);

    public char[] generatePassword(int length);

    }
