package com.divergent.mahavikreta.service.impl;

import com.divergent.mahavikreta.service.EmailUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailUtilityImpl implements EmailUtility {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMail(String toEmail,String subject,String newPassword) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("notification@mahavikreta.com");
        message.setTo(toEmail);
        message.setText(newPassword);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail sent Successfully");
    }
    @Override
    public char[] generatePassword(int length){
       System.out.println("Your new password:");
       String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
       String numbers = "0123456789";
       String pwd = letters+numbers;

        Random random = new Random();
        char[] newPass = new char[length];
        for(int i=0;i<length;i++){
            newPass[i]=pwd.charAt(random.nextInt(pwd.length()));
        }
        return newPass;
    }
}
