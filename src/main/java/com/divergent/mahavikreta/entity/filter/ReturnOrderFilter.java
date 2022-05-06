package com.divergent.mahavikreta.entity.filter;

import java.time.Instant;
import java.time.LocalDate;

import com.divergent.mahavikreta.entity.PlaceOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnOrderFilter {
	
	private Long id;
	private double discount;
	private double totalSaleAmount;
	private String remark;
	private String promoCode;
	private String status;
	private Long userId;
	private Long userAddressId;
	private LocalDate orderDate;
}
