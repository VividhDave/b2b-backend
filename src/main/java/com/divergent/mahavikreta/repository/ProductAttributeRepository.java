package com.divergent.mahavikreta.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.ProductAttribute;

@Repository
public interface ProductAttributeRepository  extends JpaRepository<ProductAttribute, Long> {

	@Query("select prodAttr from ProductAttribute prodAttr where prodAttr.product.id=?1")
	public List<ProductAttribute> findProductAttributeByProductId(Long id);
}
