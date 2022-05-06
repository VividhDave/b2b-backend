package com.divergent.mahavikreta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.divergent.mahavikreta.entity.ReturnOrderStatus;
import org.springframework.data.repository.query.Param;

public interface ReturnOrderStatusRepository extends JpaRepository<ReturnOrderStatus, Long> {

	@Query("select r from ReturnOrderStatus r where  r.returnOrder.id =:returnOrder")
	List<ReturnOrderStatus> getReturnOrderStatusByReturnorderId(@Param("returnOrder")  Long returnOrder);
	
	@Query("select returnOrderStatus from ReturnOrderStatus returnOrderStatus where returnOrderStatus.id=:id")
	List<ReturnOrderStatus> findReturnOrderStatusById(Long id);

	@Query("Select returnOrderStatus.statusMessage from  ReturnOrderStatus returnOrderStatus where returnOrderStatus.id=:key")
	List<String>findOrderMessageByOrderId(@Param("key")Long key);

}
