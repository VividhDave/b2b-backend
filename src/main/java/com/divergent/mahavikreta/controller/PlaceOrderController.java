package com.divergent.mahavikreta.controller;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.OrderStatus;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.entity.PlaceOrderProduct;
import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.entity.filter.PlaceOrderFilter;
import com.divergent.mahavikreta.payload.PlaceOrderRequest;
import com.divergent.mahavikreta.service.PlaceOrderService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

/**
 * This class provide Place order related Rest API
 * 
 * @see PlaceOrderService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.PLACE_ORDER)
public class PlaceOrderController {

	@Autowired
	public PlaceOrderService placeOrderService;

	/**
	 * This method provides an API for place Order. This method accept Post Http
	 * request with request {@link PlaceOrderRequest}.
	 * 
	 * @param request {@link PlaceOrderRequest}F
	 * @return message {@link String}
	 * 
	 * @see PlaceOrderService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<?> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
		placeOrderService.placeOrder(request, "", 0);
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.PLACE_ORDER);
	}

	/**
	 * This method provides an API for Get PlaceOrder by orderId. This method accept
	 * Get Http request with orderId and returns PlaceOrder data.
	 * 
	 * @param orderId {@link Long}
	 * @return {@link PlaceOrder}
	 * 
	 * @see PlaceOrderService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<PlaceOrder> findOrderById(@Valid @RequestParam("orderId") Long orderId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), placeOrderService.getOrderById(orderId));
	}

	/**
	 * This method provides an API for Get PlaceOrder by userId. This method accept
	 * Get Http request with userId and returns PlaceOrder data.
	 * 
	 * @param userId {@link User}
	 * @return {@link List<PlaceOrder>}
	 * 
	 * @see PlaceOrderService
	 */
	@GetMapping(UriConstants.FIND_BY_USER_ID)
	public ResponseMessage<List<PlaceOrder>> findOrderByUserId(@Valid @RequestParam("userId") Long userId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), placeOrderService.getOrderByUserId(userId));
	}

	/**
	 * This method provides an API for update place Order by order id. This method
	 * accept Put Http request with request placeOrder {@link PlaceOrder}, order id
	 * and return PlaceOrder data .
	 * 
	 * @param placeOrder {@link PlaceOrder}
	 * @param id         {@link PlaceOrder}
	 * @return {@link PlaceOrder}
	 * 
	 * @see PlaceOrderService
	 */
	@PutMapping(UriConstants.UPDATE)
	public ResponseMessage<PlaceOrder> update(@Valid @RequestBody PlaceOrder placeOrder,
			@Valid @RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), placeOrderService.update(id, placeOrder));
	}

	/**
	 * This method provides an API for Get PlaceOrder Product by orderId. This
	 * method accept Get Http request with orderId {@link PlaceOrder} and returns
	 * PlaceOrderProduct list data.
	 * 
	 * @param orderId {@link PlaceOrder}
	 * @return {@link List<PlaceOrderProduct>}
	 * 
	 * @see PlaceOrderService
	 */
	@GetMapping(UriConstants.FIND_BY_ORDER_ID)
	public ResponseMessage<List<PlaceOrderProduct>> getProductDetailsByOrderId(
			@Valid @RequestParam("orderId") Long orderId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), placeOrderService.getProductDetailsByOrderId(orderId));
	}

	/**
	 * This method provides an API for update PlaceOrder status by userId. This
	 * method accept Get Http request with userId {@link User}, status and returns
	 * PlaceOrder list data.
	 * 
	 * @param userId {@link Long}
	 * @param status {@link String}
	 * @return {@link List<PlaceOrder>}
	 * 
	 * @see PlaceOrderService
	 */
	@GetMapping(UriConstants.FIND_BY_USER_ID_AND_STATUS)
	public ResponseMessage<List<PlaceOrder>> findByUserIdAndStatus(@Valid @RequestParam("userId") Long userId,
			@Valid @RequestParam("status") String status) {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				placeOrderService.getPlaceOrderByUserIdAndStatus(userId, status));
	}

	/**
	 * This method provides an API for Get all PlaceOrder list. This method accept
	 * Post Http request with pageSize, sortOrder. sortValue and placeOrderFilter
	 * {@link PlaceOrderFilter} and return PageImpl object.
	 * 
	 * @param pageIndex        {@link Integer}
	 * @param pageSize         {@link Integer}
	 * @param sortOrder        {@link String}
	 * @param sortValue        {@link String}
	 * @param placeOrderFilter {@link PlaceOrderFilter}
	 * 
	 * @return {@link PageImpl<PlaceOrder>}
	 * @throws ParseException
	 * 
	 * @see PlaceOrderService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<PlaceOrder>> getAllOrderStatus(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue,
			@RequestBody(required = false) PlaceOrderFilter placeOrderFilter) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), placeOrderService.getAllOrder(
				ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), placeOrderFilter));
	}

	/**
	 * This method provides an API for return place Order. This method accept
	 * Post Http request with orderId {@link PlaceOrder}.
	 * 
	 * @param orderId {@link Long}
	 * @return message {@link String}
	 * 
	 * @see PlaceOrderService
	 */
	@PostMapping(UriConstants.RETURN)
	public ResponseMessage<?> returnOrder(@Valid @RequestParam("orderId") Long orderId) {
		placeOrderService.cancelOrder(orderId);
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.CANCEL_ORDER);
	}

	@GetMapping(UriConstants.ORDER_STATUS)
	public ResponseMessage<String> findByUserIdAndStatus(@Valid @RequestParam("status") String status, @Valid @RequestParam("orderId") Long key) {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				placeOrderService.getOrderStatus(status, key));
	}
}
