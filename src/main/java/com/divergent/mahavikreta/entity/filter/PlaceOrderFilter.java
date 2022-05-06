package com.divergent.mahavikreta.entity.filter;

import lombok.Data;

@Data
public class PlaceOrderFilter {
	
	private Long id;
	private double productTotalAmount;
	private double shippingCharge;
	private double discount;
	private double totalSaleAmount;
	private String remark;
	private String promoCode;
	private String status;
	private Long userId;
	private Long userAddressId;

}
