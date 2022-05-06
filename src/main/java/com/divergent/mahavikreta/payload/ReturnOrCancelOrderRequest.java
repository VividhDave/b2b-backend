package com.divergent.mahavikreta.payload;

import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ReturnOrCancelOrderRequest {

	private Long id;

	private String status;
	
	private Long orderId;

	private Long userId;

	private Set<ReturnOrCancelOrderProductRequest> returnProduct;
	
}
