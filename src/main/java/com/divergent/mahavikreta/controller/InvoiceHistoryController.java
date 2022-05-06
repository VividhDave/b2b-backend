package com.divergent.mahavikreta.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.service.InvoiceHistoryService;

/**
 * This class provide InvoiceHistory related Rest API
 * 
 * @see InvoiceHistoryService
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.PRODUCT_ORDER_HISTORY)
public class InvoiceHistoryController {

	@Autowired
	private InvoiceHistoryService invoiceHistoryService;

	/**
	 * This method provides an API to download Invoice history. This method accept Get Http
	 * request with orderId {@link PlaceOrder} and returns Invoice history Data.
	 * 
	 * @param orderId
	 * @return {@link InputStreamResource}
	 * 
	 * @see InvoiceHistoryService
	 * @throws IOException
	 */
	@GetMapping(value = UriConstants.DOWNLOAD_HISTORY, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> invoiceHistory(@Valid @RequestParam(name = "orderId") Long orderId)
			throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=invoice.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(invoiceHistoryService.downloadInvoiceHistory(orderId));
	}
}
