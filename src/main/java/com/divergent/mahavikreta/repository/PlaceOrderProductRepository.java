package com.divergent.mahavikreta.repository;

import java.util.List;

import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.divergent.mahavikreta.entity.PlaceOrderProduct;

@Repository
public interface PlaceOrderProductRepository extends JpaRepository<PlaceOrderProduct, Long> {

	@Query("select placeOrderProduct from PlaceOrderProduct placeOrderProduct where placeOrderProduct.placeOrder.id =:placeOrderId")
	List<PlaceOrderProduct> findbyPlaceOrderProductByOrderId(@Param("placeOrderId") Long placeOrderId);
	
	@Transactional
	@Modifying
	@Query("update PlaceOrderProduct p set p.orderStatus=?1 where p.placeOrder.id=?2")
	int updatePlaceOrderProductStatus(String status, Long orderId);
	
	@Transactional
	@Modifying
	@Query("update PlaceOrderProduct p set p.orderStatus=?1 where p.placeOrder.id IN ?2")
	int updatePlaceOrderProductStatusByIds(String status, List<Long> orderId);

	public PlaceOrderProduct findByPlaceOrderAndProduct(PlaceOrder placeOrder, Product product);
}
