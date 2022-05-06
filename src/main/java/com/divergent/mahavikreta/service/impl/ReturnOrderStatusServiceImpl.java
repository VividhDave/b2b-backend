package com.divergent.mahavikreta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.entity.ReturnOrCancelOrder;
import com.divergent.mahavikreta.entity.ReturnOrderStatus;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.payload.ReturnStatusRequest;
import com.divergent.mahavikreta.repository.ReturnOrCancelOrderRepository;
import com.divergent.mahavikreta.repository.ReturnOrderStatusRepository;
import com.divergent.mahavikreta.service.ReturnOrderStatusService;
import com.divergent.mahavikreta.utility.AppUtility;

@Service
public class ReturnOrderStatusServiceImpl implements ReturnOrderStatusService{

	@Autowired
	private ReturnOrCancelOrderRepository returnOrCancelOrderRepository;
	
	@Autowired
	private ReturnOrderStatusRepository returnOrderStatusRepository;
	
	@Override
	public ReturnOrderStatus save(ReturnStatusRequest returnStatus) {
		ReturnOrCancelOrder returnOrder = returnOrCancelOrderRepository.findReturnOrCancelOrderById(returnStatus.getReturnOrderId());
		if(AppUtility.isEmpty(returnOrder)) {
			throw new GenricException("Order not Returned");
		}
		ReturnOrderStatus returnOrderStatus = new ReturnOrderStatus();
		returnOrderStatus.setDescription(returnStatus.getDescription());
		returnOrderStatus.setStatusMessage(returnStatus.getStatusMessage());
		returnOrderStatus.setReturnOrder(returnOrder);
		returnOrCancelOrderRepository.updateStatus(returnStatus.getStatusMessage(), returnOrder.getId());
		return returnOrderStatusRepository.save(returnOrderStatus);
	}


	@Override
	public List<ReturnOrderStatus> getStatusByReturnOrderId(Long returnOrderId) {
		return returnOrderStatusRepository.getReturnOrderStatusByReturnorderId(returnOrderId);
	}

	@Override
	public List<ReturnOrderStatus> getStatusById(Long id) {
		if(id == null) {
			throw  new GenricException("Please enter valid Id");
		}
		return returnOrderStatusRepository.findReturnOrderStatusById(id);
	}

}
