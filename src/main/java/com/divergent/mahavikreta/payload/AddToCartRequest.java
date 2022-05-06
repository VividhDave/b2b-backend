package com.divergent.mahavikreta.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AddToCartRequest {
	    private int qty;
	    private double price;
	    private double deliveryCost;
	    private Long userid;
	    private Long productId;
}
