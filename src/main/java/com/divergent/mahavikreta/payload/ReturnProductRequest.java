package com.divergent.mahavikreta.payload;

import lombok.Data;

@Data
public class ReturnProductRequest {
	private int qty;
    private String description;
    private Long userId;
    private Long productId;
    private Long orderId;

}
