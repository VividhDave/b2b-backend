package com.divergent.mahavikreta.controller;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.RequestForQuotation;
import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.service.RequestForQuotationService;
import com.divergent.mahavikreta.utility.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * This class provide Quotation related API
 * 
 * @see RequestForQuotationService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.QUOTATION_REQUEST)
public class RequestForQuotationController {

	@Autowired
	public RequestForQuotationService quotationService;

	/**
	 * This method provides an API for save Quotation Request. This method accept Post Http
	 * request with request quotation {@link RequestForQuotation} return RequestForQuotation object.
	 * 
	 * @param quotation {@link RequestForQuotation}
	 * 
	 * @return {@link RequestForQuotation}
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<RequestForQuotation> saveQuotationRequest(
			@Valid @RequestBody RequestForQuotation quotation) {

		RequestForQuotation requestForQuotation = quotationService.saveQuotationRequest(quotation);
		if(requestForQuotation == null)
			return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.ERROR_USER_QUOTATION_EXIST,null);
		else
			return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.QUOTATION_SAVE,requestForQuotation);
	}

	/**
	 * This method provides an API for download Quotation Request. This method accept Get Http
	 * request with request productId {@link Product}, userId {@link User} return InputStreamResource file.
	 * 
	 * @param productId {@link Long} {@link Product}
	 * @param userId {@link Long} {@link User}
	 * 
	 * @return {@link InputStreamResource}
	 * @throws IOException
	 */
	@GetMapping(value = UriConstants.QUOTATION_PDF, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadQuotationReport(@Valid @RequestParam(name = "productId") Long productId,
			@Valid @RequestParam(name = "userId") Long userId) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=quotation.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(quotationService.quotationReport(productId, userId));
	}
}
