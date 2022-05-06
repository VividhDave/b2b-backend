package com.divergent.mahavikreta.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.divergent.mahavikreta.entity.Attribute;

public interface AttributeService {
	
	public Attribute save(Attribute brand);

	public PageImpl<Attribute> getList(Pageable pageable, String attributeName) throws ParseException;

	public List<Attribute> getAllRecord();

	public List<Attribute> findAttributeByCategoryId(Long id);
}
