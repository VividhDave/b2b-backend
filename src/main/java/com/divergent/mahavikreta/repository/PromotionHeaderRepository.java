package com.divergent.mahavikreta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.PromotionHeader;

import java.util.Optional;

@Repository
public interface PromotionHeaderRepository extends JpaRepository<PromotionHeader, Long>{

	@Query("select promotionHeader from PromotionHeader promotionHeader")
	Page<PromotionHeader> findAllPromotionHeader(Pageable pageable);

	PromotionHeader findPromotionHeaderById(Long productId);

	Optional<PromotionHeader> findPromotionHeaderByClaimCode(String code);
}
