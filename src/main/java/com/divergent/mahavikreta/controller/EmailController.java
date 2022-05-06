package com.divergent.mahavikreta.controller;

import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.service.EmailService;
import com.divergent.mahavikreta.utility.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UriConstants.GENERATE_EMAIL)
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping(UriConstants.SEND)
    public ResponseMessage<String>sendEmail(@RequestParam("email")String email){
        emailService.sendMail(email);
        return new ResponseMessage<>(HttpStatus.OK.value(),"Password sent to your mail");

    }
}
