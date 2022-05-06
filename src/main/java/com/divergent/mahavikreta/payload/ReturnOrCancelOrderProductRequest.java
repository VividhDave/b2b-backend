package com.divergent.mahavikreta.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ReturnOrCancelOrderProductRequest {

	private String description;

	private int qty;
	
	private double price;
	
	private Long productId;
}
