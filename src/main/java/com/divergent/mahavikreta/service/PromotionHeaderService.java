package com.divergent.mahavikreta.service;

import java.text.ParseException;
import java.util.List;

import com.divergent.mahavikreta.entity.filter.PromotionFilter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.divergent.mahavikreta.entity.PromotionHeader;


public interface PromotionHeaderService {

	PromotionHeader save(PromotionFilter promotion);

	PageImpl<PromotionHeader> getList(Pageable pageable) throws ParseException;

	List<PromotionHeader> getAllRecord();

	PromotionHeader findById(Long id);

	PromotionHeader checkClaimCode(String code);
}
