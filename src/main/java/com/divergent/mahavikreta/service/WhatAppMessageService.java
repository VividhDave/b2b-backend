package com.divergent.mahavikreta.service;

import org.springframework.http.HttpStatus;

public interface WhatAppMessageService {

	public HttpStatus sendMessage(String reciverNumber, String message, String type);

}
