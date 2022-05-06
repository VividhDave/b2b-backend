package com.divergent.mahavikreta.service;


import java.util.List;

import com.divergent.mahavikreta.entity.ReturnOrderStatus;
import com.divergent.mahavikreta.payload.ReturnStatusRequest;

public interface ReturnOrderStatusService {
	
	ReturnOrderStatus save(ReturnStatusRequest returnStatus);
	
	List<ReturnOrderStatus> getStatusByReturnOrderId(Long returnOrderId);
	
	List<ReturnOrderStatus> getStatusById(Long id);

}
