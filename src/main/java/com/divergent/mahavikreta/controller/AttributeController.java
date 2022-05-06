package com.divergent.mahavikreta.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

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
import com.divergent.mahavikreta.entity.Attribute;
import com.divergent.mahavikreta.service.AttributeService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Product Attribute related Rest API
 * 
 * @see AttributeService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.ATTRIBUTE)
public class AttributeController {
	
	@Autowired
	AttributeService attributeService;
	
	/**
	 * This method provides an API for save new Attribute. This method accept Post Http request
	 * with {@link Attribute} and return Attribute data.
	 * 
	 * 
	 * @param attribute {@link Attribute}
	 * 
	 * @see AttributeService
	 * @return {@link Attribute}
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<Attribute> save(@Valid @RequestBody Attribute attribute) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.ATTRIBUTE_CREATED_SUCCESSFULLY, attributeService.save(attribute));
	}
	
	/**
	 *  This method provides an API for get list of Attribute. This method accept Get Http request
	 *  and return list of Attribute {@link Attribute}.
	 * 
	 * @see AttributeService
	 * @return {@link Attribute}
	 */
	@GetMapping(UriConstants.GET_ALL_RECORD)
	public ResponseMessage<List<Attribute>> getAll() {
		return new ResponseMessage<>(HttpStatus.OK.value(), attributeService.getAllRecord());
	}

	/**
	 *  This method provides an API for Get all attribute data. This method accept Post Http request
	 *  with pageSize, sortOrder. sortValue and attributeName and return PageImpl object.
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param AttributeName
	 * @param sortOrder
	 * @param sortValue
	 * 
	 * @return {@link PageImpl<Attribute>}
	 * 
	 * @see AttributeService
	 * @throws ParseException
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<Attribute>> getCategoryList(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize, @Valid @RequestParam(required = false,name="attributeName") String AttributeName,
			@RequestParam(required = false,name="sortOrder") String sortOrder, @RequestParam(required = false,name="sortValue") String sortValue)
			throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				attributeService.getList(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), AttributeName));
	}
	
	/**
	 * This method provides an API for Get List of Attribute using category id. This method accept Get Http request
	 * with id {@link Category} and return List of Attribute .
	 * 
	 * 
	 * @param id
	 * @return {@link List<Attribute>}
	 * 
	 * @see AttributeService
	 */
	@GetMapping(UriConstants.GET_BY_CAT_ID)
	public ResponseMessage<List<Attribute>> getByCategoryId(@Valid @RequestParam("categoryId") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), attributeService.findAttributeByCategoryId(id));
	}

}
