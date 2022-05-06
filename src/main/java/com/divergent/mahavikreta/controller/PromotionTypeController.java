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

import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.PromotionType;
import com.divergent.mahavikreta.service.PromotionTypeService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

@RestController
@RequestMapping(UriConstants.PROMOTION_TYPE)
public class PromotionTypeController {
	
	@Autowired
	PromotionTypeService promotionTypeService;

	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<PromotionType> save(@Valid @RequestBody PromotionType promotion) {
		return new ResponseMessage<>(HttpStatus.OK.value(), "Promotion Type Created Successfully",
				promotionTypeService.save(promotion));
	}
	
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<PromotionType> save(@Valid @RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				promotionTypeService.findById(id));
	}

	@GetMapping(UriConstants.GET_ALL_RECORD)
	public ResponseMessage<List<PromotionType>> getAll() {
		return new ResponseMessage<>(HttpStatus.OK.value(), promotionTypeService.getAllRecord());
	}

	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<PromotionType>> getProductList(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), promotionTypeService
				.getList(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue)));
	}

}
