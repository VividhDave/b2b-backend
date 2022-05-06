package com.divergent.mahavikreta.service;

import java.util.List;

import com.divergent.mahavikreta.entity.ProductAttribute;

public interface ProductAttributeService {

	ProductAttribute save(ProductAttribute productAttribute);
	
	List<ProductAttribute> saveMultiRecord(List<ProductAttribute> productAttributeList);
	
	List<ProductAttribute> getByProductId(Long id);
}
