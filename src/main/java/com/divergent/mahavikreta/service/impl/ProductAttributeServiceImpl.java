package com.divergent.mahavikreta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.entity.ProductAttribute;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.ProductAttributeRepository;
import com.divergent.mahavikreta.service.ProductAttributeService;

@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {

	@Autowired
	ProductAttributeRepository productAttributeRepository;

	@Override
	public List<ProductAttribute> saveMultiRecord(List<ProductAttribute> productAttributeList) {
		return productAttributeRepository.saveAll(productAttributeList);
	}

	@Override
	public List<ProductAttribute> getByProductId(Long id) {
		if (id == null) {
			throw new GenricException("Please enter valid Id");
		}
		return productAttributeRepository.findProductAttributeByProductId(id);
	}

	@Override
	public ProductAttribute save(ProductAttribute productAttribute) {
		return null;
	}

}
