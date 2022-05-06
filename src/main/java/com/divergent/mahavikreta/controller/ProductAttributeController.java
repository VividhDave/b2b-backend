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

import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.ProductAttribute;
import com.divergent.mahavikreta.service.ProductAttributeService;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Product Attribute related API
 * 
 * @see ProductAttributeService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.PRODDUCT_ATTRIBUTE)
public class ProductAttributeController {
	
	@Autowired
	ProductAttributeService productAttributeService;
	
	/**
	 * This method provides an API for save multiple product attribute. This method accept Post Http
	 * request with request productAttributeList {@link  List<ProductAttribute>} return {@link List<ProductAttribute>}.
	 * 
	 * 
	 * @param productAttributeList {@link List<ProductAttribute>}
	 * 
	 * @return {@link List<ProductAttribute>}
	 * 
	 * @see ProductAttributeService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<List<ProductAttribute>> save(@Valid @RequestBody List<ProductAttribute> productAttributeList) {
		return new ResponseMessage<>(HttpStatus.OK.value(), "Product Attribute created successfully", productAttributeService.saveMultiRecord(productAttributeList));
	}

	/**
	 * This method provides an API for get product attribute list by product id. This method accept Get Http
	 * request with request id {@link Product} return {@link List<ProductAttribute>}.
	 * 
	 * 
	 * @param productId {@link Long} {@link Product}
	 * 
	 * @return {@link List<ProductAttribute>}
	 * 
	 * @see ProductAttributeService
	 */
	@GetMapping(UriConstants.GET_BY_PROD_ID)
	public ResponseMessage<List<ProductAttribute>> getByCategoryId(@Valid @RequestParam("productId") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), productAttributeService.getByProductId(id));
	}

}
