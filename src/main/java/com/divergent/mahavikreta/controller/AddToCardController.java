package com.divergent.mahavikreta.controller;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.AddToCard;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.entity.filter.CardFilter;
import com.divergent.mahavikreta.payload.AddToCartRequest;
import com.divergent.mahavikreta.service.AddToCardService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

/**
 * This class provide Cart related Rest API
 * 
 * @see AddToCardService
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.ADD_TO_CARD)
public class AddToCardController {

	@Autowired
	public AddToCardService addToCardService;
	
	
	/**
	 * This method provides an API for Add item to cart. This method accept Post Http request
	 * with {@link AddToCartRequest} and returns cart data.
	 * 
	 * @param addToCart {@link AddToCartRequest} 
	 * @return {@link AddToCard}
	 * 
	 * @see AddToCardService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<AddToCard> addToCard(@Valid @RequestBody AddToCartRequest addToCart) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.ADD_TO_CARD_SAVED_SUCCESSFULLY,
				addToCardService.addToCard(addToCart));
	}

	/**
	 * This method provides an API for Delete product from cart. This method accept Delete Http request
	 * with productId.
	 * 
	 * @param productId {@link Product}
	 * @param userId
	 * @return message
	 * 
	 * @see AddToCardService
	 */
	@DeleteMapping(UriConstants.DELETE)
	public ResponseMessage<AddToCard> deleteProductFromCard(@Valid @RequestParam(name = "productId") Long productId,
			@Valid @RequestParam(name = "userId") Long userId) {
		addToCardService.deleteProductFromCard(productId, userId);
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.CARD_PRODUCT_DELETE);
	}

	/**
	 * This method provides an API for Get all cart data. This method accept Post Http request
	 * with pageSize, sortOrder. sortValue and {@link CardFilter} and return PageImpl object.
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param sortOrder
	 * @param sortValue
	 * @param cardFilter
	 * 
	 * @return {@link PageImpl<AddToCard>}
	 * 
	 * @see AddToCardService
	 * @throws ParseException
	 */
	@PostMapping(UriConstants.GET_CARD_DETAIL)
	public ResponseMessage<PageImpl<AddToCard>> getCardDetailByUser(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue,
			@RequestBody(required = false) CardFilter cardFilter) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), addToCardService.getCardDetailByUser(
				ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), cardFilter));
	}

	/**
	 * This method provides an API for Get Cart detail from cart. This method accept Get Http request
	 * with userId and return list of cart data based on user id.
	 * 
	 * @param userId {@link User}
	 * @return {@link List<AddToCard>}
	 * 
	 * @see AddToCardService
	 */
	@PostMapping(UriConstants.FIND_BY_USER_ID)
	public ResponseMessage<List<AddToCard>> getCartDetails(@Valid @RequestParam(name = "userId") Long userId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), addToCardService.findCartDetailsByUserId(userId));
	}

	/**
	 * This method provides an API for Update item to cart. This method accept Post Http request
	 * with {@link AddToCartRequest} and returns cart data.
	 *
	 * @param addToCart {@link AddToCartRequest}
	 * @return {@link AddToCard}
	 *
	 * @see AddToCardService
	 */
	@PostMapping(UriConstants.UPDATE)
	public ResponseMessage<AddToCard> updateAddToCardProduct(@Valid @RequestBody AddToCartRequest addToCart) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.ADD_TO_CARD_UPDATE_SUCCESSFULLY,
				addToCardService.updateAddToCardProduct(addToCart));
	}
}
