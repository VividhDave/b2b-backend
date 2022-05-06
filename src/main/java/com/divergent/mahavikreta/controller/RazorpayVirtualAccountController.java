package com.divergent.mahavikreta.controller;

import java.io.IOException;

import javax.validation.Valid;

import com.razorpay.RazorpayException;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.RazorpayVirtualAccount;
import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.service.RazorpayVirtualAccountService;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Razorpay Virtual Account related API
 * 
 * @author Aakash
 *
 *@see RazorpayVirtualAccountService
 */
@RestController
@RequestMapping(UriConstants.RAZORPAY_VIRTUAL_ACOOUNT)
public class RazorpayVirtualAccountController {

	@Autowired
	RazorpayVirtualAccountService razorpayVirtualAccountService;

	/**
	 * This method provides an API to find virtual Account details by user id. This method accept Post Http
	 * request with request userId {@link User} return RazorpayVirtualAccount object
	 * 
	 * @param userId {@link Long}
	 * 
	 * @return {@link RazorpayVirtualAccount}
	 * @throws JsonProcessingException
	 * @throws IOException
	 * 
	 * @see RazorpayVirtualAccountService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<RazorpayVirtualAccount> findRazorpayVirtualAccountByUserId(@Valid @RequestParam("id") Long userId)
			throws JsonProcessingException, IOException, RazorpayException {
		return new ResponseMessage<>(HttpStatus.OK.value(), razorpayVirtualAccountService.findById(userId));
	}
}
