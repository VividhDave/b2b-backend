package com.divergent.mahavikreta.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.entity.Brand;

public interface BrandService {

	public Brand save(Brand brand);

	public PageImpl<Brand> getList(Pageable pageable, String brandName) throws ParseException;

	public List<Brand> getAllRecord();
	
	public Brand saveImage(MultipartFile file,Long brandId) throws IOException;
	
	public Brand findBrandById(Long Id);
}
