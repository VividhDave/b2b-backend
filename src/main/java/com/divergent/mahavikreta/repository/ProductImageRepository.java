package com.divergent.mahavikreta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

	@Query("select prodImg from ProductImage prodImg where prodImg.product.id=?1")
	public List<ProductImage> findProductImageByProductId(Long id);
}
