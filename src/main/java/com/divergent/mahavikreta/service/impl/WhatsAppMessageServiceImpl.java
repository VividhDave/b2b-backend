package com.divergent.mahavikreta.service.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.service.WhatAppMessageService;

@Service
public class WhatsAppMessageServiceImpl implements WhatAppMessageService {

	@Value("${whatsapp.apiKey}")
	public String apiKey;
	
	@Value("${whatsapp.source}")
	public String source;
	
	@Value("${whatsapp.appName}")
	public String appName;
	
	@Value("${whatsapp.channel}")
	public String channel;
	
	
	
	@Override
	public HttpStatus sendMessage(String reciverNumber, String message,String type) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("apikey", apiKey);
		
		MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
		map.add("channel", channel);
		map.add("source", source);
		map.add("destination", reciverNumber);
		map.add("src.name", appName);
		Map<String, Object> messageObject = new HashMap<>();
		messageObject.put("isHSM", false);
		messageObject.put("type", type);
		if(type.equals("text")) {
			messageObject.put("text", message);
		}else if(type.equals("image")) {
			messageObject.put("originalUrl", message);
			messageObject.put("previewUrl", message);
			messageObject.put("caption", "Sample image");
		}
		try {
			map.add("message",  objectMapper.writeValueAsString(messageObject));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(
				  AppConstants.WHATS_APP_SINGLE_URL, request , String.class);
		
		HttpStatus statusCode=response.getStatusCode();	  
		return statusCode;
	}
	
	

}
