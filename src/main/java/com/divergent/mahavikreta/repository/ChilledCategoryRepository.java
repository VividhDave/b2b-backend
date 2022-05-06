package com.divergent.mahavikreta.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.ChilledCategory;

@Repository
public interface ChilledCategoryRepository extends JpaRepository<ChilledCategory, Long> {

	@Query("select cat from ChilledCategory cat where cat.name=?1 and cat.subCategory.id=?2")
	ChilledCategory findChilledCategoryByNameAndSubCatId(String name, Long subCategoryId);
	
	@Query("select cat from ChilledCategory cat")
	Page<ChilledCategory> findAllChilledCategory(Pageable pageable);
	
	@Query("select chilledCat from ChilledCategory chilledCat where chilledCat.subCategory.id=?1")
	public List<ChilledCategory> findChilledCategoryBySubCategoryId(Long subCategoryId);
	
	ChilledCategory findChilledCategoryById(Long id);

	ChilledCategory findChilledCategoryByName(String name);

}