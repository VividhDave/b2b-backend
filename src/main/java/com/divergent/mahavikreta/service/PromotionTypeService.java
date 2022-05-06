package com.divergent.mahavikreta.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.divergent.mahavikreta.entity.PromotionType;

public interface PromotionTypeService {

	PromotionType save(PromotionType promotion);

	PageImpl<PromotionType> getList(Pageable pageable) throws ParseException;

	List<PromotionType> getAllRecord();

	PromotionType findById(Long id);
}
