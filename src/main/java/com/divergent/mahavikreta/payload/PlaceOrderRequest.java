package com.divergent.mahavikreta.payload;

import java.util.List;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class 	PlaceOrderRequest {
	private Long userId;
	private double productTotalAmount;
	private double shippingCharge;
	private double discount;
	private double totalSaleAmount;
	private String remark;
	private String promoCode;
	private Long addressId;
	private List<PlaceOrderProductRequest> placeOrderProductRequest;
	private String orderType;
}
