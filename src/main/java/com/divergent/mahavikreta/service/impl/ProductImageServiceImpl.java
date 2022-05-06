package com.divergent.mahavikreta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.ProductImage;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.ProductImageRepository;
import com.divergent.mahavikreta.repository.ProductRepository;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.ProductImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductImageServiceImpl implements ProductImageService {

	@Autowired
	ProductImageRepository productImageRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	LogService logService;

	@Override
	public ProductImage save(ProductImage productImage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductImage> getByProductId(Long id) {
		if (id == null) {
			throw new GenricException("Please enter valid Id");
		}
		return productImageRepository.findProductImageByProductId(id);
	}

	@Override
	public void deleteImageById(long id) {
		this.productImageRepository.deleteById(id);
	}

	@Override
	public List<ProductImage> saveMultiRecord(MultipartFile[] files, Long productId) {
		List<ProductImage> storedFile = new ArrayList<ProductImage>();
		Product product = productRepository.findProductById(productId);
		try {
			for (MultipartFile file : files) {
				ProductImage productImage = new ProductImage();
				productImage.setImageName(file.getOriginalFilename());
				productImage.setImage(file.getBytes());
				productImage.setProduct(product);
				storedFile.add(productImage);
			}
			productImageRepository.saveAll(storedFile);
		} catch (Exception e) {
			log.error(e.getMessage());
			logService.saveErrorLog("Error getting in save product image", "ProductImageController", "saveMultiRecord", e.getMessage());
			throw new GenricException(e.getMessage());
		}
		return storedFile;
	}

}
