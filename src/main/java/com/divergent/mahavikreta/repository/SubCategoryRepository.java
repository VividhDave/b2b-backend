package com.divergent.mahavikreta.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

	@Query("select subCat from SubCategory subCat where subCat.name=?1 and subCat.category.id=?2")
	SubCategory findSubCategoryByName(String name, Long categoryId);
	
	@Query("select subCategory from SubCategory subCategory")
	Page<SubCategory> findAllSubCategory(Pageable pageable);
	
	@Query("select subCat from SubCategory subCat where subCat.category.id=?1")
	public List<SubCategory> findSubCategoryByCategoryId(Long categoryId);
	
	SubCategory findSubCategoryById(Long id);
	
	SubCategory findSubCategoryByName(String name);
}
