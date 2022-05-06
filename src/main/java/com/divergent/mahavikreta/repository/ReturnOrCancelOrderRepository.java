package com.divergent.mahavikreta.repository;

import java.util.List;

import com.divergent.mahavikreta.entity.filter.ReturnOrderFilter;
import com.divergent.mahavikreta.payload.ReturnOrCancelOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.divergent.mahavikreta.entity.ReturnOrCancelOrder;

public interface ReturnOrCancelOrderRepository extends JpaRepository<ReturnOrCancelOrder, Long> {
	
	ReturnOrCancelOrder findReturnOrCancelOrderById(Long id);
	
	@Query("select pr from ReturnOrCancelOrder pr where pr.user.id=?1 and pr.status!=?2")
	List<ReturnOrCancelOrder> findReturnOrCancelOrderByUserIdAndStatus(Long userId,String status);
	
	@Query("select pr from ReturnOrCancelOrder pr where pr.placeOrder.id=?1")
	List<ReturnOrCancelOrder> findReturnOrCancelOrderByOrderId(Long orderId);


	@Query("select returnOrder from ReturnOrCancelOrder returnOrder where returnOrder.status not in ('Cancel','Delivered') order by returnOrder.id desc")
	Page<ReturnOrCancelOrder> findAllReturnOrCancelOrder(Pageable pageable);
	
	@Transactional
	@Modifying
	@Query("update ReturnOrCancelOrder p set p.status=?1 where p.id=?2")
	int updateStatus(String status, Long id);
}
