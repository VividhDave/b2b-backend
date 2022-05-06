package com.divergent.mahavikreta.entity.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PromotionFilter {

    private Long id;
    private String buyerPurchases;
    private Integer buyerPurchasesValue;
    private Integer productSelectionId;
    private String buyerGets;
    private Integer buyerGetsValue;
    private String appliesTo;
    private Date startDate;
    private Date endDate;
    private String internalDescription;
    private String trackingId;
    private String claimCodeType;
    private Boolean redemptionPerCustomer;
    private String claimCode;
    private String claimCodeCombinability;
    private String checkoutDisplayText;
    private String shortDisplayName;
    private String displayDescription;
}
