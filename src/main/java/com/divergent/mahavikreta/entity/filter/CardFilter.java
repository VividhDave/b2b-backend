package com.divergent.mahavikreta.entity.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardFilter {

    private Long id;
    private int qty;
    private double price;
    private double deliveryCost;
    private Boolean status = true;
    private Integer productId;
    private Integer userId;
}
