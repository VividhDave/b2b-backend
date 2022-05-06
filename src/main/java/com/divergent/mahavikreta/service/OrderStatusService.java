package com.divergent.mahavikreta.service;



import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.divergent.mahavikreta.entity.OrderStatus;
import com.divergent.mahavikreta.payload.OrderStatusRequest;

public interface OrderStatusService {

	OrderStatus save(OrderStatusRequest orderStatus);
	
	OrderStatus update(Long id,OrderStatus orderStatus);
	
	List<OrderStatus> getOrderStatusByOrderId(Long id);

//	OrderStatus getOrderStatusByUserId(Long id);
	
	PageImpl<OrderStatus> getAllOrderStatus(Pageable pageable, Long id);
	
}
