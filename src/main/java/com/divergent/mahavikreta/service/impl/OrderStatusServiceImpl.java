package com.divergent.mahavikreta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.divergent.mahavikreta.entity.OrderStatus;
import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.payload.OrderStatusRequest;
import com.divergent.mahavikreta.repository.OrderStatusRepository;
import com.divergent.mahavikreta.repository.PlaceOrderProductRepository;
import com.divergent.mahavikreta.repository.PlaceOrderRepository;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.OrderStatusService;
import com.divergent.mahavikreta.utility.AppUtility;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class OrderStatusServiceImpl implements OrderStatusService {

	@Autowired
	OrderStatusRepository orderStatusRepository;

	@Autowired
	PlaceOrderRepository placeOrderRepository;

	@Autowired
	PlaceOrderProductRepository placeOrderProductRepository;

	@Autowired
	LogService logService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OrderStatus save(OrderStatusRequest orderStatus) {
		PlaceOrder placeOrder =placeOrderRepository.findPlaceOrderById(orderStatus.getOrderId());
		if(AppUtility.isEmpty(placeOrder)) {
			throw new GenricException("Order not valid");
		}
		try {
		OrderStatus order=new OrderStatus();
		order.setDescription(orderStatus.getDescription());
		order.setNotificationSend(false);
		order.setStatusMessage(orderStatus.getStatusMessage());
		order.setPlaceOrder(placeOrder);
		placeOrderRepository.updatePlaceOrderStatus(orderStatus.getStatusMessage(),placeOrder.getId());
		placeOrderProductRepository.updatePlaceOrderProductStatus(orderStatus.getStatusMessage(), placeOrder.getId());
		return orderStatusRepository.save(order);
		}catch(Exception ex) {
			log.error(ex.getMessage());
			logService.saveErrorLog("Error in saving the orderProduct status", "OrderStatusController", "save", ex.getMessage());
			throw new GenricException("Error in saving the orderProduct status");
		}
	}

	@Override
	public OrderStatus update(Long id, OrderStatus orderStatus) {
		if (id != null || AppUtility.isEmpty(orderStatus)) {
			throw new GenricException("Id is null");
		}
		return orderStatusRepository.save(orderStatus);
	}

	@Override
	public List<OrderStatus> getOrderStatusByOrderId(Long id) {
		if (id == null) {
			throw new GenricException("Please enter valid Id");
		}
		return orderStatusRepository.findOrderStatusByOrderId(id);
	}

	@Override
	public PageImpl<OrderStatus> getAllOrderStatus(Pageable pageable, Long id) {
		try {
		List<OrderStatus> workEffortMaps = null;
		long count;
		Page<OrderStatus> page;
		page = orderStatusRepository.findAllOrderStatus(pageable, id);
		workEffortMaps = (List<OrderStatus>) page.getContent();
		count = page.getTotalElements();
		return new PageImpl<>(workEffortMaps, pageable, count);
		}catch(Exception ex) {
			log.error(ex.getMessage());
			logService.saveErrorLog("Error in get all order status", "OrderStatusController", "getAllOrderStatus", ex.getMessage());
			throw new GenricException("Error in get all order status"+ex.getMessage());
		}
	}

}
