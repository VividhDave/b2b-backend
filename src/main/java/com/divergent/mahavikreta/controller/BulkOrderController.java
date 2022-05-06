package com.divergent.mahavikreta.controller;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.BulkOrder;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.filter.BulkOrderFilter;
import com.divergent.mahavikreta.entity.filter.ProductFilter;
import com.divergent.mahavikreta.service.BulkOrderService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(UriConstants.BULK_ORDER)
//@Component
public class BulkOrderController {

    @Autowired
    BulkOrderService bulkOrderService;

    @PostMapping(UriConstants.SAVE)
    public ResponseMessage<BulkOrder> save(@Valid @RequestBody BulkOrderFilter bulkOrderFilter ) {
        return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.BULK_ORDER_CREATED_SUCCESSFULLY,
                bulkOrderService.save(bulkOrderFilter));
    }

    @GetMapping(UriConstants.BULK_ORDER_BY_STATUS)
    public ResponseMessage<List<BulkOrder>> getPendingBulkOrder(@RequestParam("status") Boolean status,
                                                                @RequestParam("orderStatus") String orderStatus,
                                                                @RequestParam(required = false, name = "id") Long id) {
        return new ResponseMessage<>(HttpStatus.OK.value(),
                bulkOrderService.getPendingBulkOrder(status, orderStatus, id));
    }

    @PostMapping(UriConstants.APPROVE_REJECT)
    public ResponseMessage<BulkOrder> approveOrRejectOrder(@RequestParam("id") Long id,
                                                           @RequestParam("status") Boolean status,
                                                           @RequestParam("orderStatus") String orderStatus,
                                                           @RequestParam(required = false, name = "negotiablePrice") Double negotiablePrice,
                                                           @RequestParam(required = false, name = "comment") String comment) {
        return new ResponseMessage<>(HttpStatus.OK.value(), bulkOrderService.approveOrRejectOrder(id, status, orderStatus, negotiablePrice, comment));
    }

    @PutMapping(UriConstants.BULK_ORDER_PURCHASE)
    public ResponseMessage<BulkOrder> bulkOrderPurchasing(@RequestParam("id") Long id,
                                                          @RequestParam("addressId") Long addressId,
                                                          @RequestParam("remark") String remark) {
        return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.BULK_ORDER_PURCHASE_SUCCESSFULLY,
                bulkOrderService.bulkOrderPurchasing(id, addressId, remark));
    }

    @GetMapping(UriConstants.BULK_ORDER_BY_USER)
    public ResponseMessage<List<BulkOrder>> getBulkOrderByUser(@RequestParam("userId") Long userId) {
        return new ResponseMessage<>(HttpStatus.OK.value(),
                bulkOrderService.getBulkOrderByUser(userId));
    }

    @PostMapping(UriConstants.ACCEPT_REJECT_BY_USER)
    public ResponseMessage<BulkOrder> acceptOrRejectByUser(@RequestParam("id") Long id,
                                                           @RequestParam("orderStatus") String orderStatus) {
        return new ResponseMessage<>(HttpStatus.OK.value(), bulkOrderService.acceptOrRejectByUser(id, orderStatus));
    }

    @GetMapping(UriConstants.BULK_ORDER_EDIT)
    public ResponseMessage<BulkOrder> editBulkOrder(@RequestParam("status") Boolean status,
                                                                @RequestParam("orderStatus") String orderStatus,
                                                                @RequestParam("id") Long id) {
        return new ResponseMessage<>(HttpStatus.OK.value(),
                bulkOrderService.editBulkOrder(status, orderStatus, id));
    }

    @PostMapping(UriConstants.GET_ALL)
    public ResponseMessage<PageImpl<BulkOrder>> getAllBulkOrder(@RequestParam("pageIndex") int pageIndex,
                                                             @RequestParam("pageSize") int pageSize,
                                                             @RequestParam(required = false, name = "sortOrder") String sortOrder,
                                                             @RequestParam(required = false, name = "sortValue") String sortValue,
                                                             @RequestBody(required = false) BulkOrderFilter filter) throws ParseException {
        return new ResponseMessage<>(HttpStatus.OK.value(), bulkOrderService.getAllBulkOrder(
                ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), filter));
    }

}
