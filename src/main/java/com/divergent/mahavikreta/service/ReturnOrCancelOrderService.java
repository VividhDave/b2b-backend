package com.divergent.mahavikreta.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.divergent.mahavikreta.entity.ReturnOrCancelOrder;
import com.divergent.mahavikreta.entity.ReturnOrCancelOrderProduct;
import com.divergent.mahavikreta.entity.filter.ReturnOrderFilter;
import com.divergent.mahavikreta.payload.ReturnOrCancelOrderRequest;

public interface ReturnOrCancelOrderService {

	public ReturnOrCancelOrder returnOrder(ReturnOrCancelOrderRequest returnOrCancelOrderRequest);
	
	public ReturnOrCancelOrder cancelOrder(Long orderId);
	
	public PageImpl<ReturnOrCancelOrder> findAll(Pageable pageable,ReturnOrderFilter returnOrderFilter)  throws ParseException;
	
	public List<ReturnOrCancelOrder> findByUserId(Long userId);
	
	public ReturnOrCancelOrder findByReturnId(Long id);
	
	public List<ReturnOrCancelOrderProduct> findReturnOrCancelOrderProductByReturnId(Long id);

	String getOrderStatus(String status, Long key);
}

