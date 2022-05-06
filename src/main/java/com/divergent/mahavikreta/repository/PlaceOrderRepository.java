package com.divergent.mahavikreta.repository;

import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.entity.PlaceOrderProduct;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PlaceOrderRepository extends JpaRepository<PlaceOrder, Long> {

	@Query("select placeOrder from PlaceOrder placeOrder where placeOrder.user.id=:userId")
	List<PlaceOrder> findByUserId(@Param("userId") Long userId);

	@Query("select placeOrder from PlaceOrder placeOrder where placeOrder.id = :orderId")
	Optional<PlaceOrder> findByOrderId(@Param("orderId") Long orderId);

	@Query("select placeOrder from PlaceOrder placeOrder Order by placeOrder.id DESC")
	Page<PlaceOrder> findAllOrder(Pageable pageable);

	@Query("select placeOrderProduct from PlaceOrderProduct placeOrderProduct where placeOrderProduct.placeOrder.id=:orderId")
	List<PlaceOrderProduct> getProductDetailsByOrderId(@Param("orderId") Long orderId);

	PlaceOrder findPlaceOrderById(Long id);
	
	@Query("select placeOrder from PlaceOrder placeOrder where placeOrder.user.id=?1 and placeOrder.status=?2")
	List<PlaceOrder> findPlaceOrderByUserId(Long userId, String orderStatus);

	@Transactional
	@Modifying
	@Query("update PlaceOrder p set p.payedAmount=?1, p.payed=?2 where p.id=?3")
	int updatePlaceOrderPaymentPayedByCustomer(double payedAmount, boolean payed, Long id);

	@Transactional
	@Modifying
	@Query("update PlaceOrder p set p.status=?1 where p.id=?2")
	int updatePlaceOrderStatus(String status, Long id);

	
	@Query("select p.id from PlaceOrder p where DATEDIFF(now(),p.orderDate)>1 and p.status='Payment Pending'")
	List<Long> findPlaceOrderByOrderDateAndStatus();
	
	@Transactional
	@Modifying
	@Query("update PlaceOrder p set p.status=?1 where p.id IN ?2")
	int updatePlaceOrderStatusByIds(String status, List<Long> orderId);

}
