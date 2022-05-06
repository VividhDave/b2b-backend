package com.divergent.mahavikreta.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.entity.ProductImage;

public interface ProductImageService {
	
	ProductImage save(ProductImage productImage);
	
	List<ProductImage> saveMultiRecord(MultipartFile[] files,Long productId);
	
	List<ProductImage> getByProductId(Long id);

    void deleteImageById(long id);
}
