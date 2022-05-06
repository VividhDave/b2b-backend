package com.divergent.mahavikreta.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.OrderStatus;


@Repository
public interface OrderStatusRepository  extends JpaRepository<OrderStatus, Long> {

	@Query("select orderStatus from OrderStatus orderStatus where orderStatus.placeOrder.id=:orderId")
    List<OrderStatus> findOrderStatusByOrderId(Long orderId);

	@Query("select orderStatus from OrderStatus orderStatus where orderStatus.placeOrder.id=:orderId")
	Page<OrderStatus> findAllOrderStatus(Pageable pageable, @Param("orderId")Long orderId);
	
	@Query("select orderStatus from OrderStatus orderStatus where orderStatus.placeOrder.id=:orderId and orderStatus.statusMessage=:status")
	OrderStatus findOrderStatusByOrderIdAndStatus(Long orderId, String status);

	@Query("Select orderStatus.statusMessage from OrderStatus orderStatus where orderStatus.placeOrder.id=:key")
    List<String> findStatusMessageById (Long key);
}
