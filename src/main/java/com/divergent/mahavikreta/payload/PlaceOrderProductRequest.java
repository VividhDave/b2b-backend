package com.divergent.mahavikreta.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PlaceOrderProductRequest {
	private Long productId;
	private int qty;
}
