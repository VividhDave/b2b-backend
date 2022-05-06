package com.divergent.mahavikreta.service;

import com.divergent.mahavikreta.entity.RefundPayment;

public interface RefundPaymentService {

	public RefundPayment save(String paymentId, Long userId, Long orderId,int payment,Long refundId);
}
