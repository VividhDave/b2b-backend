package com.divergent.mahavikreta.service;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;

public interface InvoiceHistoryService {

	InputStreamResource downloadInvoiceHistory(Long orderid) throws IOException;
}
