package com.divergent.mahavikreta.service;


import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.entity.PlaceOrderProduct;
import com.divergent.mahavikreta.entity.filter.PlaceOrderFilter;
import com.divergent.mahavikreta.payload.PlaceOrderRequest;

public interface PlaceOrderService {

	public void placeOrder(PlaceOrderRequest request, String constStr, double bulkOrderPrice);

	public List<PlaceOrder> getOrderByUserId(Long userId);

	PlaceOrder update(Long id, PlaceOrder placeOrder);

	PageImpl<PlaceOrder> getAllOrder(Pageable pageable, PlaceOrderFilter placeOrderFilter) throws ParseException;

	List<PlaceOrderProduct> getProductDetailsByOrderId(Long orderId);

	List<PlaceOrder> getPlaceOrderByUserIdAndStatus(Long userId, String status);

	PlaceOrder getOrderById(Long orderId);

	public void cancelOrder(Long orderId);

	String getOrderStatus(String status, Long key);
}
