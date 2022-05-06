package com.divergent.mahavikreta.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.ReturnOrderStatus;
import com.divergent.mahavikreta.payload.ReturnStatusRequest;
import com.divergent.mahavikreta.service.ReturnOrderStatusService;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Return Order Status API
 * 
 * @see ReturnOrderStatusService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.RETURN_STATUS)
public class ReturnOrderStatusController {

	@Autowired
	private ReturnOrderStatusService returnOrderStatusService;

	/**
	 * This method provides an API for save return order status. This method accept
	 * Post Http request with request returnStatus {@link ReturnStatusRequest}
	 * return ReturnOrderStatus.
	 * 
	 * @param returnStatus {@link ReturnStatusRequest}
	 * @return {@link ReturnStatusRequest}
	 * 
	 * @see ReturnOrderStatusService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<ReturnOrderStatus> saveReturnOrderStatus(
			@Valid @RequestBody ReturnStatusRequest returnStatus) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.RETURN_ORDER_SAVED,
				returnOrderStatusService.save(returnStatus));
	}

	/**
	 * This method provides an API for get return order status by id. This method
	 * accept Get Http request with request id return ReturnOrderStatus.
	 * 
	 * @param id {@link Long}
	 * @return {@link ReturnOrderStatus}
	 * 
	 * @see ReturnOrderStatusService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<List<ReturnOrderStatus>> getReturnOrderStatusById(@Valid @RequestParam("id") Long id) {
		return new ResponseMessage<List<ReturnOrderStatus>>(HttpStatus.OK.value(), returnOrderStatusService.getStatusById(id));
	}

	/**
	 * This method provides an API for get return order status by order id. This
	 * method accept Get Http request with request id return list ReturnOrderStatus.
	 * 
	 * @param returnOrderId
	 * @return {@link List}
	 * 
	 * @see ReturnOrderStatusService
	 */
	@GetMapping(UriConstants.GET_RETURNORDERSTATUS_BY_RETURNORDER_ID)
	public ResponseMessage<List<ReturnOrderStatus>> getReturnOrderStatusByReturnOrderId(
			@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@Valid @RequestParam("return_id") Long returnOrderId) {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				returnOrderStatusService.getStatusByReturnOrderId(returnOrderId));
	}

}
