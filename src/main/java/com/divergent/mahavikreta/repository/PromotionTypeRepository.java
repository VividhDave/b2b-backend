package com.divergent.mahavikreta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.PromotionType;

@Repository
public interface PromotionTypeRepository extends JpaRepository<PromotionType, Long> {
	
	@Query("select promotionType from PromotionType promotionType")
	Page<PromotionType> findAllPromotionType(Pageable pageable);

	PromotionType findPromotionTypeById(Long promotionTypeId);
	
	PromotionType findPromotionTypeByPromoTypeCode(String code);

}
