package com.divergent.mahavikreta.service;

import java.io.IOException;

import com.divergent.mahavikreta.entity.User;
import com.razorpay.RazorpayException;
import org.codehaus.jackson.JsonProcessingException;

import com.divergent.mahavikreta.entity.RazorpayVirtualAccount;
import com.divergent.mahavikreta.entity.RefundPayment;

public interface RazorpayVirtualAccountService {
	
	
	public RazorpayVirtualAccount createVirtualAccount(Long userId) throws JsonProcessingException, IOException, RazorpayException;
	
	public RazorpayVirtualAccount findById(Long userId) throws JsonProcessingException, IOException, RazorpayException;
	
	public RefundPayment refundAmount(String paymentId, Long userId,Long orderId,int payment,Long refundId);

	public User createRazorpayCustomer(User user) throws RazorpayException;

}
