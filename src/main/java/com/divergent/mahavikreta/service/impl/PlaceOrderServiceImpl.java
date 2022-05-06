package com.divergent.mahavikreta.service.impl;

import com.divergent.mahavikreta.constants.AppConstants;
// import com.divergent.mahavikreta.constants.OrderType;
import com.divergent.mahavikreta.entity.OrderStatus;
import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.entity.PlaceOrderProduct;
import com.divergent.mahavikreta.entity.Product;

import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.entity.UserAddress;
import com.divergent.mahavikreta.entity.filter.PlaceOrderFilter;

import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.mapper.PlaceOrderMapper;
import com.divergent.mahavikreta.payload.PlaceOrderProductRequest;
import com.divergent.mahavikreta.payload.PlaceOrderRequest;
import com.divergent.mahavikreta.repository.AddToCardRepository;
import com.divergent.mahavikreta.repository.OrderStatusRepository;
import com.divergent.mahavikreta.repository.PlaceOrderProductRepository;
import com.divergent.mahavikreta.repository.PlaceOrderRepository;
import com.divergent.mahavikreta.repository.ProductRepository;
import com.divergent.mahavikreta.repository.UserAddressRepository;
import com.divergent.mahavikreta.repository.UserRepository;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.PlaceOrderService;
import com.divergent.mahavikreta.utility.AppUtility;

import com.divergent.mahavikreta.utility.YellowMessengerAPIIntegration;
import lombok.extern.log4j.Log4j2;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import javax.persistence.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class PlaceOrderServiceImpl implements PlaceOrderService {

    @Autowired
    public PlaceOrderRepository placeOrderRepository;

    @Autowired
    public PlaceOrderProductRepository placeProductOrderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAddressRepository userAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    AddToCardRepository addToCardRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    LogService logService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(PlaceOrderRequest request, String constStr, double bulkOrderPrice) {
        try {
            if (AppUtility.isEmpty(request.getPlaceOrderProductRequest())) {
                throw new GenricException("Please Add Product");
            }
            PlaceOrder placeOrder = new PlaceOrder();
            User user = userRepository.findUserById(request.getUserId());
            if (AppUtility.isEmpty(user)) {
                throw new GenricException("User Not exist");
            }
            if (!constStr.equals(AppConstants.BULK_ORDER)) {
                if (!AppUtility
                        .isEmpty(placeOrderRepository.findPlaceOrderByUserId(user.getId(), AppConstants.PAYMENT_PENDING))) {
                    throw new GenricException("Please Do Payment of previous order then place new order");
                }
            }
            placeOrder.setUser(user);
            UserAddress userAddress = userAddressRepository.findUserAddressByIdAndUserId(request.getAddressId(),
                    user.getId());
            if (AppUtility.isEmpty(userAddress)) {
                throw new GenricException("User Address Not exist");
            }
            placeOrder.setDeliveryAddress(userAddress);
            placeOrder.setProductTotalAmount(Double.parseDouble(new DecimalFormat("#.##").format(request.getProductTotalAmount())));
            placeOrder.setDiscount(request.getDiscount());
            placeOrder.setShippingCharge(request.getShippingCharge());
            placeOrder.setTotalSaleAmount(request.getTotalSaleAmount());
            placeOrder.setPromoCode(request.getPromoCode());
            placeOrder.setOrderDate(LocalDate.now());
            placeOrder.setStatus(AppConstants.PAYMENT_PENDING);
            //----------------Difference between bulkorder and normal order----------------
            // if (constStr.equals(AppConstants.BULK_ORDER)) {
            //     placeOrder.setOrderType(OrderType.BULK_ORDER);
            // } else {
            //     placeOrder.setOrderType(OrderType.NORMAL_ORDER);
            // }
            placeOrder = placeOrderRepository.save(placeOrder);
            savePlaceOrderProductDetails(request.getPlaceOrderProductRequest(), placeOrder, constStr, bulkOrderPrice);
            addToCardRepository.deleteByUserId(user.getId());
            Calendar calender = Calendar.getInstance();
            calender.setTime(placeOrder.getCreatedDate());
            calender.add(Calendar.DATE, 10);
            String dilevryDate;
            dilevryDate=new SimpleDateFormat("MM/dd/yyyy").format(calender.getTime());
            YellowMessengerAPIIntegration.orderReceiveAPI(user.getWhatsappMobileNumber(), user.getFirstName(),dilevryDate);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            logService.saveErrorLog("Error in Placing the order", "PlaceOrderController", "placeOrder", e.getMessage());
            throw new GenricException("Order Not Placed Due to error " + e.getMessage());
        }
    }

    public void savePlaceOrderProductDetails(List<PlaceOrderProductRequest> productDetails, PlaceOrder placeOrder,
                                             String constStr, double bulkOrderPrice) {
        List<PlaceOrderProduct> placeOrderProductList = new ArrayList<>();
        try {
            double productTotalAmount = 0;
            for (PlaceOrderProductRequest object : productDetails) {
                PlaceOrderProduct placeOrderProduct = new PlaceOrderProduct();
                Product pr = productRepository.findProductById(object.getProductId());
                if (AppUtility.isEmpty(pr)) {
                    throw new GenricException("Product Not exist");
                }
                if (object.getQty() > pr.getQty()) {
                    throw new GenricException("This " + pr.getDisplayName()
                            + " product not available for this quantity qty=" + object.getQty());
                }
                if (constStr.equals(AppConstants.BULK_ORDER)) {
                    productTotalAmount = productTotalAmount + (object.getQty() * bulkOrderPrice);
                } else {
                    productTotalAmount = productTotalAmount + (object.getQty() * pr.getDiscountPrice());
                }
                placeOrderProduct.setPrice(pr.getPrice());
                placeOrderProduct.setProduct(pr);
                placeOrderProduct.setQty(object.getQty());
                placeOrderProduct.setShippingCharge(placeOrder.getShippingCharge());
                placeOrderProduct.setPlaceOrder(placeOrder);
                placeOrderProduct.setOrderStatus(placeOrder.getStatus());
                placeOrderProductList.add(placeOrderProduct);
            }
                productTotalAmount = Math.round(productTotalAmount * 100.00) / 100.00;
            if (productTotalAmount != placeOrder.getProductTotalAmount()) {
                throw new GenricException("Product Total Amount Not Match");
            }
            if ((productTotalAmount + placeOrder.getShippingCharge()) != placeOrder
                    .getTotalSaleAmount()) {
                throw new GenricException("Product Sale Total Amount Not Match");
            }
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setDescription("Order Placed And payment intitiated");
            orderStatus.setNotificationSend(false);
            orderStatus.setStatusMessage(AppConstants.PAYMENT_PENDING);
            orderStatus.setPlaceOrder(placeOrder);
            orderStatusRepository.save(orderStatus);
            placeProductOrderRepository.saveAll(placeOrderProductList);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            logService.saveErrorLog("Error in Placing the order", "PlaceOrderController", "placeOrder", ex.getMessage());
            throw new GenricException("Order Not Placed Due to error " + ex.getMessage());
        }
    }

    @Override
    public List<PlaceOrder> getOrderByUserId(Long userId) {
        return placeOrderRepository.findByUserId(userId);
    }

    private List<Predicate> setAdvanceSeachForPlaceOrder(CriteriaBuilder builder, Root<PlaceOrder> root,
                                                         PlaceOrderFilter placeOrderFilter, Join<PlaceOrder, UserAddress> userAddressJoin,
                                                         Join<PlaceOrder, User> userJoin) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        if (!AppUtility.isEmpty(placeOrderFilter.getProductTotalAmount())
                && placeOrderFilter.getProductTotalAmount() > 0)
            predicates.add(builder.equal(root.get("productTotalAmount"), placeOrderFilter.getProductTotalAmount()));

        if (!AppUtility.isEmpty(placeOrderFilter.getShippingCharge()) && placeOrderFilter.getShippingCharge() > 0)
            predicates.add(builder.equal(root.get("shippingCharge"), placeOrderFilter.getShippingCharge()));

        if (!AppUtility.isEmpty(placeOrderFilter.getDiscount()) && placeOrderFilter.getDiscount() > 0)
            predicates.add(builder.equal(root.get("discount"), placeOrderFilter.getDiscount()));

        if (!AppUtility.isEmpty(placeOrderFilter.getTotalSaleAmount()) && placeOrderFilter.getTotalSaleAmount() > 0)
            predicates.add(builder.equal(root.get("totalSaleAmount"), placeOrderFilter.getTotalSaleAmount()));

        if (!AppUtility.isEmpty(placeOrderFilter.getRemark()))
            predicates.add(builder.like(builder.lower(root.get("remark")),
                    "%" + placeOrderFilter.getRemark().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(placeOrderFilter.getPromoCode()))
            predicates.add(builder.like(builder.lower(root.get("promoCode")),
                    "%" + placeOrderFilter.getPromoCode().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(placeOrderFilter.getStatus()))
            predicates.add(builder.like(builder.lower(root.get("status")),
                    "%" + placeOrderFilter.getStatus().toLowerCase() + "%"));

        if (placeOrderFilter.getUserAddressId() != null)
            predicates.add(builder.equal(userAddressJoin.get("id"), placeOrderFilter.getUserAddressId()));

        if (placeOrderFilter.getUserId() != null)
            predicates.add(builder.equal(userJoin.get("id"), placeOrderFilter.getUserId()));

        return predicates;
    }

    @Override
    public PageImpl<PlaceOrder> getAllOrder(Pageable pageable, PlaceOrderFilter placeOrderFilter)
            throws ParseException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> criteria = builder.createTupleQuery();

        Root<PlaceOrder> root = criteria.from(PlaceOrder.class);
        Join<PlaceOrder, User> userJoin = root.join("user", JoinType.LEFT);
        Join<PlaceOrder, UserAddress> userAddressJoin = root.join("deliveryAddress", JoinType.LEFT);

        List<PlaceOrder> workEffortMaps = null;
        long count = 0;
        Page<PlaceOrder> page;
        try {
            if (AppUtility.isEmpty(placeOrderFilter)) {
                page = placeOrderRepository.findAllOrder(pageable);
                for(int i=0; i<page.getContent().size(); i++) {
                    page.getContent().get(i).getUser().setUserAddresses(null);
                    page.getContent().get(i).getDeliveryAddress().setUser(null);
                    page.getContent().get(i).setPlaceOrderProduct(null);
                }
                workEffortMaps = (List<PlaceOrder>) page.getContent();
                count = page.getTotalElements();
            } else {
                criteria.multiselect(root.get("id"), root.get("productTotalAmount"), root.get("shippingCharge"),
                        root.get("discount"), root.get("totalSaleAmount"), root.get("remark"), root.get("promoCode"),
                        root.get("status"), userJoin.get("id"), userJoin.get("username"), userJoin.get("email"),
                        userAddressJoin.get("id"), userAddressJoin.get("address"), userAddressJoin.get("city"));

                List<Predicate> predicates = setAdvanceSeachForPlaceOrder(builder, root, placeOrderFilter, userAddressJoin,
                        userJoin);

                criteria.where(predicates.toArray(new Predicate[]{})).distinct(true);

                List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
                        .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                        .setMaxResults(pageable.getPageSize()).getResultList();
                workEffortMaps = new ArrayList<>();
                for (Tuple tuple : workEffortTuples) {
                    PlaceOrder placeOrder = new PlaceOrder();
                    placeOrder.setId((Long) tuple.get(0));
                    placeOrder.setProductTotalAmount((double) tuple.get(1));
                    placeOrder.setShippingCharge((double) tuple.get(2));
                    placeOrder.setDiscount((double) tuple.get(3));
                    placeOrder.setTotalSaleAmount((double) tuple.get(4));
                    placeOrder.setRemark((String) tuple.get(5));
                    placeOrder.setPromoCode((String) tuple.get(6));

                    placeOrder.setStatus((String) tuple.get(7));

                    User user = new User();
                    user.setId((Long) tuple.get(8));
                    user.setUsername((String) tuple.get(9));
                    user.setEmail((String) tuple.get(10));

                    UserAddress userAddres = new UserAddress();
                    userAddres.setId((Long) tuple.get(11));
                    userAddres.setAddress((String) tuple.get(12));
                    userAddres.setCity((String) tuple.get(13));

                    workEffortMaps.add(placeOrder);
                }
                count = entityManager.createQuery(criteria).getResultList().size();
            }
        } catch (Exception ex) {
//            logService.saveErrorLog("Error getting placeOrder list", "PlaceOrderController", "getAllOrder", ex.getMessage());
//            throw new GenricException("Error getting list" + ex.getMessage());
            ex.printStackTrace();
        }
        return new PageImpl<>(workEffortMaps, pageable, count);
    }

    @Override
    public PlaceOrder update(Long id, PlaceOrder placeOrder) {
        if (id != null || AppUtility.isEmpty(placeOrder)) {
            throw new GenricException("Id is null");
        }
        return placeOrderRepository.save(placeOrder);
    }

    @Override
    public List<PlaceOrderProduct> getProductDetailsByOrderId(Long id) {
        return placeOrderRepository.getProductDetailsByOrderId(id);
    }

    @Override
    public PlaceOrder getOrderById(Long orderId) {
        return placeOrderRepository.findPlaceOrderById(orderId);
    }

    @Override
    public List<PlaceOrder> getPlaceOrderByUserIdAndStatus(Long userId, String status) {
        return placeOrderRepository.findPlaceOrderByUserId(userId, status);
    }

    @Override
    public void cancelOrder(Long orderId) {
        try {
            PlaceOrder placeOrder = placeOrderRepository.findPlaceOrderById(orderId);
            if (AppUtility.isEmpty(placeOrder)) {
                throw new GenricException("Wrong Order");
            }
            placeOrderRepository.updatePlaceOrderStatus(AppConstants.CANCEL, orderId);
            OrderStatus order = new OrderStatus();
            order.setDescription("Order Cancel");
            order.setNotificationSend(false);
            order.setStatusMessage(AppConstants.CANCEL);
            order.setPlaceOrder(placeOrder);
            orderStatusRepository.save(order);
        } catch (Exception ex) {
            logService.saveErrorLog("Error getting CancelOrder in place order", "PlaceOrderController", "cancelOrder", ex.getMessage());
            throw new GenricException("Error getting CancelOrder" + ex.getMessage());
        }
    }

    //----------------------Order Status-----------------------
    @Override
    public String getOrderStatus(String status, Long key) {
        List<String> values = Arrays.asList(AppConstants.PAYMENT_PENDING, AppConstants.PAYMENT_SUCCESS, AppConstants.PLACED, AppConstants.PACKED, AppConstants.IN_TRANSIT,AppConstants.ARRIVED, AppConstants.DELIVERED,AppConstants.CANCEL);
        Map<String, Integer> map = new HashMap<>();
        int i = 0;
        for (String o : values) {
            map.put(o.toString(), i);
            i++;
        }
        String currentOrderStatus = orderStatusRepository.findStatusMessageById(key).get(0);
        if (map.get(status) > map.get(currentOrderStatus)){
            OrderStatus orderStatus = this.orderStatusRepository.findOrderStatusByOrderId(key).get(0);
            orderStatus.setStatusMessage(status);
            PlaceOrder placeOrder = this.placeOrderRepository.findPlaceOrderById(key);
            placeOrder.setStatus(status);
            this.orderStatusRepository.save(orderStatus);
            this.placeOrderRepository.save(placeOrder);
            return "status successfully changed";
        }
        return "status not changed";
    }


    @Scheduled(cron = "0 0 */1 * * *")
    public void cancelOrderSchedular() {
        try {
            List<Long> ids = placeOrderRepository.findPlaceOrderByOrderDateAndStatus();
            placeOrderRepository.updatePlaceOrderStatusByIds(AppConstants.CANCEL, ids);
            placeProductOrderRepository.updatePlaceOrderProductStatusByIds(AppConstants.CANCEL, ids);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            logService.saveErrorLog("Error getting in cancel the place order ", "PlaceOrderController", "cancelOrderSchedular", ex.getMessage());
        }
    }

}
