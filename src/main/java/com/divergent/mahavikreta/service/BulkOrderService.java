package com.divergent.mahavikreta.service;

import com.divergent.mahavikreta.entity.BulkOrder;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.entity.filter.BulkOrderFilter;
import com.divergent.mahavikreta.entity.filter.ProductFilter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;

public interface BulkOrderService {

    BulkOrder save(BulkOrderFilter bulkOrderFilter);

    List<BulkOrder> getPendingBulkOrder(Boolean status, String orderStatus, Long id);

    BulkOrder approveOrRejectOrder(Long id, Boolean status, String orderStatus, Double negotiablePrice, String comment);

    BulkOrder bulkOrderPurchasing(Long id, Long addressId, String remark);

    List<BulkOrder> getBulkOrderByUser(Long userId);

    BulkOrder acceptOrRejectByUser(Long id, String orderStatus);

    BulkOrder editBulkOrder(Boolean status, String orderStatus, Long id);

    PageImpl<BulkOrder> getAllBulkOrder(Pageable pageable, BulkOrderFilter filter) throws ParseException;

    Boolean checkPermitOrder(int userId, String orderStatus);
}

