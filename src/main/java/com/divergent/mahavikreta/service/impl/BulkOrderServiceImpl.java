package com.divergent.mahavikreta.service.impl;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.constants.OrderStatus;
import com.divergent.mahavikreta.entity.*;
import com.divergent.mahavikreta.entity.filter.BulkOrderFilter;
import com.divergent.mahavikreta.entity.filter.ProductFilter;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.payload.PlaceOrderProductRequest;
import com.divergent.mahavikreta.payload.PlaceOrderRequest;
import com.divergent.mahavikreta.repository.BulkOrderRepository;
import com.divergent.mahavikreta.repository.ProductRepository;
import com.divergent.mahavikreta.repository.UserRepository;
import com.divergent.mahavikreta.service.BulkOrderService;
import com.divergent.mahavikreta.service.PlaceOrderService;
import com.divergent.mahavikreta.utility.AppUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.*;

@Service
public class BulkOrderServiceImpl implements BulkOrderService {

    @Autowired
    BulkOrderRepository bulkOrderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceOrderService placeOrderService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BulkOrder save(BulkOrderFilter bulkOrderFilter) {

        BulkOrder bulkOrder = new BulkOrder();

        if (!AppUtility.isEmpty(bulkOrderFilter)) {

            BulkOrder order = bulkOrderRepository.checkDuplicateBulkOrder(false, OrderStatus.REQUEST_BY_USER.toString(),
                    Long.parseLong(bulkOrderFilter.getUserId().trim()), Long.parseLong(bulkOrderFilter.getProductId().trim()));

            if (AppUtility.isEmpty(order)) {
                Product product = null;
                User user = null;

                if (!AppUtility.isEmpty(bulkOrderFilter.getProductId())) {
                    product = productRepository.findProductById(Long.parseLong(bulkOrderFilter.getProductId().trim()));
                } else {
                    throw new GenricException("Product Not Found");
                }

                if (!AppUtility.isEmpty(bulkOrderFilter.getUserId())) {
                    user = userRepository.findUserById(Long.parseLong(bulkOrderFilter.getUserId().trim()));
                } else {
                    throw new GenricException("User Not Found");
                }

                if(product.getQty() < Integer.parseInt(bulkOrderFilter.getQuantity())){

                    throw new GenricException("Quantity exceeded");
                }
               if(product.getPrice() < Double.parseDouble(bulkOrderFilter.getUserNegotiatePrice())){

                   throw new GenricException("Negotialble price connot be greater then Actual price");
               }

                bulkOrder.setDescription(bulkOrderFilter.getDescription());
                bulkOrder.setStatus(false);
                bulkOrder.setUserNegotiatePrice(Double.parseDouble(bulkOrderFilter.getUserNegotiatePrice()));
                bulkOrder.setOrderStatus(OrderStatus.REQUEST_BY_USER.toString());
                bulkOrder.setAdminNegotiatePrice(0);
                bulkOrder.setComment("");
                bulkOrder.setQuantity(Integer.parseInt(bulkOrderFilter.getQuantity()));


                User u = new User();
                u.setId(user.getId());
                bulkOrder.setUser(user);

                Product p = new Product();
                p.setId(product.getId());
                bulkOrder.setProduct(p);

                bulkOrderRepository.save(bulkOrder);
            } else {
                throw new GenricException("You cannot make more than one request for a specific product");
            }
        }

        return bulkOrder;
    }
    @Override
    public List<BulkOrder> getPendingBulkOrder(Boolean status, String orderStatus, Long id) {

        List<BulkOrder> bulkOrder;
        Boolean aBoolean = checkOrderStatus(orderStatus);

        if (aBoolean) {
            if (AppUtility.isEmpty(id)) {
                bulkOrder = bulkOrderRepository.findByStatusAndOrderStatus(status, orderStatus.trim());
            } else {
                bulkOrder = bulkOrderRepository.findByStatusAndOrderStatusAndId(status, orderStatus.trim(), id);
            }
        } else {
            throw new GenricException("Data Not Found");
        }
        return bulkOrder;
    }
    //---------------------------Check Order Permit--------------------------------------------
    @Override
    public Boolean checkPermitOrder(int userId, String orderStatus) {

        Long list = this.bulkOrderRepository.checkPermitOrder(userId);
        if(list>=5){
            return false;
        }
        return true;
    }

    public static boolean checkOrderStatus(String orderStatus) {
        for (OrderStatus or : OrderStatus.values()) {
            if (or.name().equals(orderStatus)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BulkOrder approveOrRejectOrder(Long id, Boolean status, String orderStatus, Double negotiablePrice, String comment) {

        BulkOrder bulkOrder = null;
        Boolean aBoolean = checkOrderStatus(orderStatus);

        if (!AppUtility.isEmpty(id) && aBoolean) {
            bulkOrder = bulkOrderRepository.getBulkOrderById(id, false, OrderStatus.REQUEST_BY_USER.toString());

            if (!AppUtility.isEmpty(bulkOrder)) {
                bulkOrder.setStatus(status);
                bulkOrder.setOrderStatus(orderStatus);
                if (!AppUtility.isEmpty(negotiablePrice)) {
                    bulkOrder.setAdminNegotiatePrice(negotiablePrice);
                    bulkOrder.setComment(comment);
                }
            } else {
                throw new GenricException("Empty Order");
            }
        }

        return bulkOrderRepository.save(bulkOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BulkOrder bulkOrderPurchasing(Long id, Long addressId, String remark) {

        BulkOrder bulkOrder = null;

        if (!AppUtility.isEmpty(id)) {
            bulkOrder = bulkOrderRepository.checkBulkOrderForOrderPlace(id);

            User user;
            Product product;

            if (AppUtility.isEmpty(bulkOrder)) {
                throw new GenricException("Please Approve Order First");
            } else {
                user = userRepository.findUserById(bulkOrder.getUser().getId());
                product = productRepository.findProductById(bulkOrder.getProduct().getId());

                if (!AppUtility.isEmpty(user) && !AppUtility.isEmpty(product)) {
                    if (bulkOrder.getQuantity() <= product.getQty()) {

                        PlaceOrderRequest request = new PlaceOrderRequest();
                        request.setUserId(user.getId());
                        request.setDiscount(product.getDiscountPrice());
                        request.setShippingCharge(product.getShippingCharge());
                        
                        request.setRemark(remark);
                        request.setAddressId(addressId);

                        PlaceOrderProductRequest placeOrderProductRequest = new PlaceOrderProductRequest();
                        placeOrderProductRequest.setProductId(product.getId());
                        placeOrderProductRequest.setQty(bulkOrder.getQuantity());

                        request.setPlaceOrderProductRequest(Arrays.asList(placeOrderProductRequest));

                        if (bulkOrder.getAdminNegotiatePrice() > 0 && bulkOrder.getOrderStatus().equals(OrderStatus.PRICE_NEGOTIABLE_BY_ADMIN.toString())) {
                            request.setProductTotalAmount(bulkOrder.getQuantity() * bulkOrder.getAdminNegotiatePrice());
                            request.setTotalSaleAmount(request.getProductTotalAmount() + request.getShippingCharge());
                            placeOrderService.placeOrder(request, AppConstants.BULK_ORDER, bulkOrder.getAdminNegotiatePrice());
                        } else {
                            if (bulkOrder.getOrderStatus().equals(OrderStatus.APPROVE_BY_ADMIN.toString())) {
                                request.setProductTotalAmount(bulkOrder.getQuantity() * bulkOrder.getUserNegotiatePrice());
                                request.setTotalSaleAmount(request.getProductTotalAmount() + request.getShippingCharge());
                                placeOrderService.placeOrder(request, AppConstants.BULK_ORDER, bulkOrder.getUserNegotiatePrice());
                            }
                        }
                    } else {
                        throw new GenricException("This " + product.getDisplayName()
                                + " product not available for this quantity qty=" + bulkOrder.getQuantity());
                    }

                } else {
                    throw new GenricException("User or Product Not Found");
                }
            }
        }

        return bulkOrder;
    }

    @Override
    public List<BulkOrder> getBulkOrderByUser(Long userId) {

        List<BulkOrder> bulkOrder;

        if (!AppUtility.isEmpty(userId)) {
            User user = userRepository.findUserById(userId);
            if (AppUtility.isEmpty(user)) {
                throw new GenricException("User Not Found");
            } else {
                bulkOrder = bulkOrderRepository.getBulkOrderByUser(user.getId());
            }
        } else {
            throw new GenricException("Invalid USer ID");
        }
        return bulkOrder;
    }

    @Override
    public BulkOrder acceptOrRejectByUser(Long id, String orderStatus) {

        BulkOrder bulkOrder = null;
        Boolean aBoolean = checkOrderStatus(orderStatus);

        if (!AppUtility.isEmpty(id) && aBoolean) {
            bulkOrder = bulkOrderRepository.getBulkOrderById(id);

            if (!AppUtility.isEmpty(bulkOrder)) {
                bulkOrder.setStatus(true);
                bulkOrder.setOrderStatus(orderStatus);
            } else {
                throw new GenricException("Empty Order");
            }
        }

        return bulkOrderRepository.save(bulkOrder);
    }

    @Override
    public BulkOrder editBulkOrder(Boolean status, String orderStatus, Long id) {

        BulkOrder bulkOrder;
        Boolean aBoolean = checkOrderStatus(orderStatus);

        if (aBoolean) {
            bulkOrder = bulkOrderRepository.getEditBulkOrder(status, orderStatus.trim(), id);
        } else {
            throw new GenricException("Data Not Found");
        }
        return bulkOrder;
    }

    @Override
    public PageImpl<BulkOrder> getAllBulkOrder(Pageable pageable, BulkOrderFilter filter)
            throws ParseException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<BulkOrder> criteria = builder.createQuery(BulkOrder.class);

            Root<BulkOrder> root = criteria.from(BulkOrder.class);
            Join<BulkOrder, User> userJoin = root.join("user", JoinType.INNER);
            Join<BulkOrder, Product> productJoin = root.join("product", JoinType.INNER);
            List<BulkOrder> workEffortMaps = null;
            long count;
            Page<BulkOrder> page;
            if (AppUtility.isEmpty(filter)) {
                page = bulkOrderRepository.findAllBulkOrder(pageable);
                workEffortMaps = (List<BulkOrder>) page.getContent();
                count = page.getTotalElements();
            } else {

                List<Predicate> predicates = setAdvanceSearchForBulkOrder(builder, root, userJoin, productJoin, filter);

                criteria.where(predicates.toArray(new Predicate[]{})).distinct(true);

                workEffortMaps = entityManager.createQuery(criteria)
                        .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                        .setMaxResults(pageable.getPageSize()).getResultList();
                count = entityManager.createQuery(criteria).getResultList().size();

            }

            return new PageImpl<>(workEffortMaps, pageable, count);
        } catch (Exception ex) {
            throw new GenricException(ex.getMessage());
        }
    }

    private List<Predicate> setAdvanceSearchForBulkOrder(CriteriaBuilder builder, Root<BulkOrder> root,
                                                         Join<BulkOrder, User> userJoin, Join<BulkOrder, Product> productJoin,
                                                         BulkOrderFilter filter) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();

        if (!AppUtility.isEmpty(filter.getUserName()))
            predicates.add(builder.like(builder.lower(userJoin.get("userName")),
                    "%" + filter.getUserName().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(filter.getProductName()))
            predicates.add(builder.like(builder.lower(productJoin.get("productName")),
                    "%" + filter.getProductName().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(filter.getDescription()))
            predicates.add(builder.like(builder.lower(root.get("description")),
                    "%" + filter.getDescription().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(filter.getUserNegotiatePrice()) && filter.getUserNegotiatePrice().length() > 0)
            predicates.add(builder.equal(root.get("userNegotiatePrice"), filter.getUserNegotiatePrice()));

        if (!AppUtility.isEmpty(filter.getAdminNegotiatePrice()) && filter.getAdminNegotiatePrice().length() > 0)
            predicates.add(builder.equal(root.get("adminNegotiatePrice"), filter.getAdminNegotiatePrice()));

        if (!AppUtility.isEmpty(filter.getQuantity()) && filter.getQuantity().length() > 0)
            predicates.add(builder.equal(root.get("quantity"), filter.getQuantity()));

        if (!AppUtility.isEmpty(filter.getOrderStatus()))
            predicates.add(builder.like(builder.lower(root.get("orderStatus")),
                    "%" + filter.getOrderStatus().toLowerCase() + "%"));

        return predicates;
    }
}
