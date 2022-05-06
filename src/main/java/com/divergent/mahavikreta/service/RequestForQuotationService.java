package com.divergent.mahavikreta.service;

import com.divergent.mahavikreta.entity.RequestForQuotation;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;

public interface RequestForQuotationService {

    RequestForQuotation saveQuotationRequest(RequestForQuotation quotation);

    InputStreamResource quotationReport(Long productId, Long userId) throws IOException;
}
