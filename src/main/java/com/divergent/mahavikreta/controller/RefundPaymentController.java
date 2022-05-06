package com.divergent.mahavikreta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.RefundPayment;
import com.divergent.mahavikreta.service.RefundPaymentService;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Refund payment related API
 * 
 * @see RefundPaymentService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.REFUND)
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
public class RefundPaymentController {

	@Autowired
	RefundPaymentService refundPaymentService;

	/**
	 * This method provide an API to refund the payment. This method accept Post
	 * Http request with request paymentId, userId, orderId, payment, returnId
	 * return details of RefundPayment
	 * 
	 * 
	 * @param paymentId {@link String}
	 * @param userId {@link Long}
	 * @param orderId {@link Long}
	 * @param payment {@link Double}
	 * @param returnId {@link Long}
	 * 
	 * @return {@link RefundPayment}
	 * 
	 * @see RefundPaymentService
	 */
	@PostMapping(UriConstants.PAYMENT)
	public ResponseMessage<RefundPayment> refundPayment(
			@RequestParam(name = "paymentId") String paymentId,
			@RequestParam(name = "userId") Long userId,
			@RequestParam(name = "orderId") Long orderId,
			@RequestParam(name = "payment") int payment,
			@RequestParam(name = "returnId") Long returnId) {
		refundPaymentService.save(paymentId, userId, orderId, payment, returnId);
		return new ResponseMessage<>(HttpStatus.OK.value(), "Refund Process successfully");
	}
}
