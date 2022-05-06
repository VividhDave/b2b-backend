package com.divergent.mahavikreta.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import com.divergent.mahavikreta.entity.filter.PromotionFilter;
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
import com.divergent.mahavikreta.entity.PromotionHeader;
import com.divergent.mahavikreta.service.PromotionHeaderService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

@RestController
@RequestMapping(UriConstants.PROMOTION_HEADER)
public class PromotionHeaderController {
	@Autowired
	PromotionHeaderService promotionHeaderService;

	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<PromotionHeader> save(@Valid @RequestBody PromotionFilter promotion) {
		System.out.println(promotion.toString());
		return new ResponseMessage<>(HttpStatus.OK.value(), "Promotion Created Successfully",
				promotionHeaderService.save(promotion));
	}
	
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<PromotionHeader> save(@Valid @RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				promotionHeaderService.findById(id));
	}

	@PostMapping(UriConstants.GET_ALL_RECORD)
	public ResponseMessage<List<PromotionHeader>> getAll() {
		return new ResponseMessage<>(HttpStatus.OK.value(), promotionHeaderService.getAllRecord());
	}

	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<PromotionHeader>> getPromotionHeaderList(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), promotionHeaderService.getList(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue)));
	}

	@GetMapping(UriConstants.CODE)
	public ResponseMessage<PromotionHeader> checkClaimCode(@Valid @RequestParam("code") String code) {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				promotionHeaderService.checkClaimCode(code));
	}
}
