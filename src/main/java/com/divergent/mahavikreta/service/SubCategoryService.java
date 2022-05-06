package com.divergent.mahavikreta.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.entity.SubCategory;

public interface SubCategoryService {

	SubCategory save(SubCategory subcategory);

	PageImpl<SubCategory> getList(Pageable pageable, String categoryName) throws ParseException;

	List<SubCategory> getAllRecord();
	
	List<SubCategory> getByCategoryId(Long categoryId);
	
	public SubCategory saveImage(MultipartFile file,Long subCategoryId) throws IOException;
	
	public SubCategory findSubCategoryById(Long id);

    void deleteSubCategoryById(long id);
}
