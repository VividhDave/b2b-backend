package com.divergent.mahavikreta.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.entity.Category;

public interface CategoryService {
	
	Category save(Category category);
	
	PageImpl<Category> getList(Pageable pageable,String categoryName)throws ParseException;
	
	List<Category> getAllRecord();
	
	public Category saveImage(MultipartFile file,Long categoryId) throws IOException;

	public Category findCategoryById(Long id);

    void deleteCategoryById(long id);
}
