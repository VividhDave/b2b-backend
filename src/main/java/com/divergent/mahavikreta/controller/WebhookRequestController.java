package com.divergent.mahavikreta.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.divergent.mahavikreta.entity.filter.PaymentFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.Payment;
import com.divergent.mahavikreta.service.WebhookRequestService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

import javax.validation.Valid;

/**
 * This class provide webhook related API
 * 
 * @see WebhookRequestService
 * 
 * @author Aakash
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/webhook")
public class WebhookRequestController {

	@Autowired
	WebhookRequestService webhookRequestService;

	/**
	 * This method provide an API to fetch payment from razorpay. This method accept
	 * Post Http request with request,headers {@link HttpHeaders} and return
	 * true/false
	 * 
	 * @param request {@link String}
	 * @param headers {@link HttpHeaders}
	 * @return true/false {@link Boolean}
	 */
	@PostMapping("/payment")
	public boolean getAllPayment(@RequestBody String request, @RequestHeader HttpHeaders headers) {
		log.info("request : " + request);
		log.info("headers : " + headers.toString());
		return webhookRequestService.verifyPayment(request, headers);
	}

	/**
	 * This method provides an API for Get all Payment list. This method accept Post
	 * Http request with pageSize, sortOrder, sortValue and return PageImpl object.
	 * 
	 * @param pageIndex {@link Integer}
	 * @param pageSize  {@link Integer}
	 * @param sortOrder {@link String}
	 * @param sortValue {@link String}
	 * 
	 * @return {@link PageImpl}
	 * @throws ParseException
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<Map<String, String>>> getCategoryList(@RequestParam("pageIndex") int pageIndex,
																		  @RequestParam("pageSize") int pageSize,
																		  @RequestParam(required = false, name = "sortOrder") String sortOrder,
																		  @RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), webhookRequestService
				.getList(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue)));
	}

	@PostMapping(UriConstants.GET_BY_EMAIL)
	public ResponseMessage<List<Payment>> getByUserName(@Valid @RequestParam("email")String email){
		return new ResponseMessage<List<Payment>>(HttpStatus.OK.value(),webhookRequestService.getPaymentListByEmail(email));
	}


}

