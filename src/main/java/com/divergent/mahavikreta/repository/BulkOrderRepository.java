package com.divergent.mahavikreta.repository;

import com.divergent.mahavikreta.entity.BulkOrder;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BulkOrderRepository extends JpaRepository<BulkOrder, Long> {

    List<BulkOrder> findByStatusAndOrderStatus(Boolean status, String orderStatus);

    List<BulkOrder> findByStatusAndOrderStatusAndId(Boolean status, String orderStatus, Long id);

    @Query("select bulkOrder from BulkOrder bulkOrder where bulkOrder.id=:id and bulkOrder.status=:status " +
            "and bulkOrder.orderStatus=:orderStatus")
    BulkOrder getBulkOrderById(Long id, Boolean status, String orderStatus);

    @Query("select bulkOrder from BulkOrder bulkOrder where bulkOrder.user.id=:userId order by bulkOrder.lastModifiedDate desc")
    List<BulkOrder> getBulkOrderByUser(Long userId);

    @Query("select bulkOrder from BulkOrder bulkOrder where bulkOrder.id=:id")
    BulkOrder getBulkOrderById(Long id);

    @Query("select bulkOrder from BulkOrder bulkOrder where bulkOrder.status=:status and bulkOrder.orderStatus=:orderStatus " +
            "and bulkOrder.id=:id")
    BulkOrder getEditBulkOrder(Boolean status, String orderStatus, Long id);

    @Query(nativeQuery = true, value = "select * from bulk_order where status=true and " +
            "order_status in ('APPROVE_BY_ADMIN','PRICE_NEGOTIABLE_BY_ADMIN') and id = ?1")
    BulkOrder checkBulkOrderForOrderPlace(Long id);

    @Query(nativeQuery = true, value = "select * from bulk_order bo inner join product p on p.id=bo.product_id " +
            "inner join users u on u.id=bo.user_id where bo.status= ?1 and bo.order_status= ?2 and bo.user_id= ?3 " +
            "and bo.product_id= ?4")
    BulkOrder checkDuplicateBulkOrder(Boolean status, String orderStatus, Long userId, Long productId);

    @Query("select bulkOrder from BulkOrder bulkOrder order by bulkOrder.id desc")
    Page<BulkOrder> findAllBulkOrder(Pageable pageable);

    @Query(nativeQuery = true, value = "select count(order_status) from bulk_order where user_id=:userId AND order_status !='REQUEST_BY_USER' AND 'ACCEPT_BY_USER'")
    public Long checkPermitOrder(@Param("userId") int userId);
}



