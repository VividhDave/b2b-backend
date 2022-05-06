package com.divergent.mahavikreta.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.divergent.mahavikreta.entity.ReturnOrCancelOrderProduct;

public interface ReturnOrCancelOrderProductRepository extends JpaRepository<ReturnOrCancelOrderProduct, Long> {
	
	@Query("select pr from ReturnOrCancelOrderProduct pr where pr.returnOrCancelOrder.id=?1")
	List<ReturnOrCancelOrderProduct> findReturnOrCancelOrderProductByReturnOrderId(Long id);

}
