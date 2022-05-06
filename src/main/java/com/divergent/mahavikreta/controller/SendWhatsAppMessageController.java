package com.divergent.mahavikreta.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.service.WhatAppMessageService;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide sending the what's app message API 
 * 
 * @see WhatAppMessageService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping("api/v1/msg")
public class SendWhatsAppMessageController {

	@Autowired
	WhatAppMessageService whatAppMessageService;

	/**
	 * This method provide an API to send message on what's app
	 * 
	 * @param value {@link Map}
	 * @return {@link HttpStatus}
	 */
	@PostMapping("/send")
	public ResponseMessage<HttpStatus> sendWhatsAppMessage(@Valid @RequestBody Map<String, Object> value) {
		return new ResponseMessage<HttpStatus>(HttpStatus.OK.value(), "Message Send Successfully",
				whatAppMessageService.sendMessage((String) value.get("reciverNumber"), (String) value.get("message"),
						(String) value.get("type")));
	}
}
