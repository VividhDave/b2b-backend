package com.divergent.mahavikreta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.entity.RefundPayment;
import com.divergent.mahavikreta.repository.RefundPaymentRepository;
import com.divergent.mahavikreta.service.RazorpayVirtualAccountService;
import com.divergent.mahavikreta.service.RefundPaymentService;

@Service
public class RefundPaymentServiceImpl implements RefundPaymentService {

	@Autowired
	RefundPaymentRepository refundPaymentRepository;

	@Autowired
	RazorpayVirtualAccountService razorpayVirtualAccountService;

	@Override
	public RefundPayment save(String paymentId, Long userId, Long orderId,int payment,Long refundId) {
		return razorpayVirtualAccountService.refundAmount(paymentId, userId, orderId,payment,refundId);
	}

}
