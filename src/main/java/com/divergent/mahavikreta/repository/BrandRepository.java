package com.divergent.mahavikreta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>{

	Boolean existsByName(String brandName);
	
	@Query("select brand from Brand brand")
	Page<Brand> findAllBrand(Pageable pageable);
	
	Brand findBrandByName(String brandName);
	
	Brand findBrandById(Long id);
}
