package com.divergent.mahavikreta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	Boolean existsByName(String categoryName);
	
	@Query("select category from Category category")
	Page<Category> findAllCategory(Pageable pageable);
	
	Category findCategoryById(Long id);
	
	Category findCategoryByName(String name);
}
