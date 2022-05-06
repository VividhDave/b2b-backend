package com.divergent.mahavikreta.entity.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuotationFilter {

    private String rfqCode;
    private int minQuantity;
    private int maxQuantity;
    private double preferredUnitPrice;
    private String details;
    private boolean status = true;
    private Integer productId;
    private Integer userId;
}
