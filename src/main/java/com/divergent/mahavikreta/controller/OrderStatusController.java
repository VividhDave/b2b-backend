package com.divergent.mahavikreta.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.OrderStatus;
import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.payload.OrderStatusRequest;
import com.divergent.mahavikreta.service.OrderStatusService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Order Status related Rest API
 * 
 * @see OrderStatusService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.ORDER_STATUS)
public class OrderStatusController {

	@Autowired
	public OrderStatusService orderStatusService;

	/**
	 * This method provides an API for Add OrderStatus. This method accept Post Http
	 * request with orderStatus {@link OrderStatusRequest} and returns orderStatus
	 * data.
	 * 
	 * @param orderStatus {@link OrderStatusRequest}
	 * @return {@link OrderStatus}
	 * 
	 * @see OrderStatusService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<OrderStatus> saveOrderStatus(@Valid @RequestBody OrderStatusRequest orderStatus) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.PLACE_ORDER,
				orderStatusService.save(orderStatus));
	}

	/**
	 * This method provides an API for Update OrderStatus. This method accept Put
	 * Http request with orderStatus {@link OrderStatusRequest}, id and returns
	 * orderStatus data.
	 * 
	 * @param orderStatus {@link OrderStatus}
	 * @param id
	 * @return {@link OrderStatus}
	 * 
	 * @see OrderStatusService
	 */
	@PutMapping(UriConstants.UPDATE)
	public ResponseMessage<?> update(@Valid @RequestBody OrderStatus orderStatus, @Valid @RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.ORDERSTATUS_UPDATE_SUCCESSFULLY,
				orderStatusService.update(id, orderStatus));
	}

	/**
	 * This method provides an API for Get OrderStatus by order id. This method
	 * accept Get Http request with orderId {@link PlaceOrder} and returns list of
	 * orderStatus.
	 *
	 * 
	 * @param orderId
	 * @return {@link List<OrderStatus>}
	 * 
	 * @see OrderStatusService
	 */
	@GetMapping(UriConstants.GET_ORDERSTATUS_BY_ORDER_ID)
	public ResponseMessage<List<OrderStatus>> getOrderStatusByOrderId(@Valid @RequestParam("orderId") Long orderId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), orderStatusService.getOrderStatusByOrderId(orderId));
	}

	/**
	 * This method provides an API for Get all OrderStatus list. This method accept
	 * Post Http request with pageSize, sortOrder. sortValue and
	 * return PageImpl object.
	 * 
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param sortOrder
	 * @param sortValue
	 * 
	 * @return {@link PageImpl<OrderStatus>}
	 * @throws ParseException
	 * 
	 * @see OrderStatusService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<OrderStatus>> getAllOrderStatus(
			@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue,
			@RequestParam(required = false, name = "orderId") Long id) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), orderStatusService
				.getAllOrderStatus(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), id));
	}

}
