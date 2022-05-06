package com.divergent.mahavikreta.service.impl;

import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.UserRepository;
import com.divergent.mahavikreta.service.EmailService;
import com.divergent.mahavikreta.service.EmailUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    EmailUtility emailUtility;

    @Override
    public void sendMail(String email) {
        String password = new String(emailUtility.generatePassword(9));
        User user = userRepository.findByEmail(email).orElseThrow(()-> new GenricException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        emailUtility.sendMail(email,"Your new password","Hello User,\n\n " +
                "On Mahavikreta account forgot password was requested, please verify your account with below new Password:\t"+password.trim()+"\n\n Regards,\n" +
                "Mahavikreta Team");

//        ,"Hello User,\n" +
//                "On Mahavikreta account forgot password was requested, please verify your account with below new Password:"
//        ,"Regards,\n" +
//                "Mahavikreta Team"

    }
}
