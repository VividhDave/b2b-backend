package com.divergent.mahavikreta.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.divergent.mahavikreta.entity.filter.PaymentFilter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import com.divergent.mahavikreta.entity.Payment;

public interface WebhookRequestService {
	
	public boolean verifyPayment(String body,HttpHeaders header);
	
	PageImpl<Map<String, String>> getList(Pageable pageable) throws ParseException;

	List getPaymentListByEmail(String email);
}
