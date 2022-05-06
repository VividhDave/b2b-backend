package com.divergent.mahavikreta.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.entity.ReturnOrCancelOrder;
import com.divergent.mahavikreta.entity.ReturnOrCancelOrderProduct;
import com.divergent.mahavikreta.entity.filter.ReturnOrderFilter;
import com.divergent.mahavikreta.payload.ReturnOrCancelOrderRequest;
import com.divergent.mahavikreta.service.ReturnOrCancelOrderService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Return or cancel order related API
 * 
 * @see ReturnOrCancelOrderService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.RETURN_OR_CANCEL_ORDER)
public class ReturnOrCancelOrderController {

	@Autowired
	private ReturnOrCancelOrderService returnOrCancelOrderService;

	/**
	 * This method provides an API for return order. This method accept Post Http
	 * request with request returnOrCancelOrderRequest
	 * {@link ReturnOrCancelOrderRequest} return ReturnOrCancelOrder object.
	 * 
	 * 
	 * @param returnOrCancelOrderRequest {@link ReturnOrCancelOrderRequest}
	 * @return {@link ReturnOrCancelOrder}
	 * 
	 * @see ReturnOrCancelOrderService
	 */
	@PostMapping(UriConstants.RETURN)
	public ResponseMessage<ReturnOrCancelOrder> returnOrder(
			@Valid @RequestBody ReturnOrCancelOrderRequest returnOrCancelOrderRequest) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.RETURN_ORDER_SAVED,
				returnOrCancelOrderService.returnOrder(returnOrCancelOrderRequest));
	}

	/**
	 * This method provides an API for cancel order. This method accept Post Http
	 * request with request orderId {@link PlaceOrder} return ReturnOrCancelOrder
	 * object.
	 * 
	 * @param orderId {@link Long}
	 * @return {@link ReturnOrCancelOrder}
	 * 
	 * @see ReturnOrCancelOrderService
	 */
	@PostMapping(UriConstants.CANCEL)
	public ResponseMessage<ReturnOrCancelOrder> cancelOrder(@RequestParam("orderId") Long orderId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.CANCEL_ORDER,
				returnOrCancelOrderService.cancelOrder(orderId));
	}

	/**
	 * This method provides an API for get all return or cancel order list. This
	 * method accept Post Http request with pageSize, sortOrder, sortValue and
	 * returnOrderFilter {@link ReturnOrderFilter} and return PageImpl object.
	 * 
	 * @param pageIndex         {@link Integer}
	 * @param pageSize          {@link Integer}
	 * @param sortOrder         {@link String}
	 * @param sortValue         {@link String}
	 * 
	 * @param returnOrderFilter {@link ReturnOrderFilter}
	 * 
	 * @return {@link PageImpl}
	 * @throws ParseException
	 * 
	 * @see ReturnOrCancelOrderService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<ReturnOrCancelOrder>> getAllReturnOrCancelOrder(
			@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue,
			@RequestBody(required = false) ReturnOrderFilter returnOrderFilter) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), returnOrCancelOrderService.findAll(
				ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), returnOrderFilter));
	}

	/**
	 * This method provides an API for find Return Or Cancel Order list by user id.
	 * This method accept Get Http request with request userId {@link User} return
	 * list of ReturnOrCancelOrder.
	 * 
	 * @param userId {@link Long}
	 * @return {@link List}
	 * 
	 * @see ReturnOrCancelOrderService
	 */
	@GetMapping(UriConstants.FIND_BY_USER_ID)
	public ResponseMessage<List<ReturnOrCancelOrder>> findReturnOrCancelOrderByUserId(
			@RequestParam("userId") Long userId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), returnOrCancelOrderService.findByUserId(userId));
	}

	/**
	 * This method provides an API for find Return Or Cancel Order list by return
	 * id. This method accept Get Http request with request returnId
	 * {@link ReturnOrCancelOrder} return ReturnOrCancelOrder object.
	 * 
	 * @param returnId {@link Long}
	 * @return {@link ReturnOrCancelOrder}
	 * 
	 * @see ReturnOrCancelOrderService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<ReturnOrCancelOrder> findReturnOrCancelOrderById(@RequestParam("returnId") Long returnId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), returnOrCancelOrderService.findByReturnId(returnId));
	}

	/**
	 * This method provides an API for find Return Or Cancel Order product detail list by return id.
	 * This method accept Get Http request with request returnId {@link ReturnOrCancelOrder} return
	 * list of ReturnOrCancelOrderProduct.
	 * 
	 * @param returnId {@link Long}
	 * @return {@link List}
	 * 
	 * @see ReturnOrCancelOrderService
	 */
	@GetMapping(UriConstants.PRODUCT_DETAIL)
	public ResponseMessage<List<ReturnOrCancelOrderProduct>> findReturnOrCancelOrderProductDetailByRetunrId(
			@RequestParam("returnId") Long returnId) {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				returnOrCancelOrderService.findReturnOrCancelOrderProductByReturnId(returnId));
	}

	@GetMapping(UriConstants.ORDER_STATUS)
	public ResponseMessage<String> findByUserIdAndReturnStatus(@Valid @RequestParam("status")String status, @Valid @RequestParam("orderId") Long key){
		return new ResponseMessage<>(HttpStatus.OK.value(),returnOrCancelOrderService.getOrderStatus(status, key));
	}

}
