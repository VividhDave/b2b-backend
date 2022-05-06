package com.divergent.mahavikreta.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.entity.ChilledCategory;

public interface ChilledCategoryService {

	ChilledCategory save(ChilledCategory chilledCategory);

	PageImpl<ChilledCategory> getList(Pageable pageable, String categoryName) throws ParseException;

	List<ChilledCategory> getAllRecord();
	
	List<ChilledCategory> getBySubCategoryId(Long id);
	
	public ChilledCategory saveImage(MultipartFile file,Long chilledCategoryId) throws IOException;

	public ChilledCategory findChilledCategoryById(Long id);
}
