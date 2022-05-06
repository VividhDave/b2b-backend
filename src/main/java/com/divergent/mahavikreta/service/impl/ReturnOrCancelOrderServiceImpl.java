package com.divergent.mahavikreta.service.impl;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.divergent.mahavikreta.entity.*;
import com.divergent.mahavikreta.service.RazorpayVirtualAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.entity.filter.ReturnOrderFilter;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.payload.ReturnOrCancelOrderProductRequest;
import com.divergent.mahavikreta.payload.ReturnOrCancelOrderRequest;
import com.divergent.mahavikreta.repository.OrderStatusRepository;
import com.divergent.mahavikreta.repository.PlaceOrderProductRepository;
import com.divergent.mahavikreta.repository.PlaceOrderRepository;
import com.divergent.mahavikreta.repository.ProductRepository;
import com.divergent.mahavikreta.repository.ReturnOrCancelOrderProductRepository;
import com.divergent.mahavikreta.repository.ReturnOrCancelOrderRepository;
import com.divergent.mahavikreta.repository.ReturnOrderStatusRepository;
import com.divergent.mahavikreta.repository.UserRepository;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.ReturnOrCancelOrderService;
import com.divergent.mahavikreta.utility.AppUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReturnOrCancelOrderServiceImpl implements ReturnOrCancelOrderService {

	@Autowired
	PlaceOrderRepository placeOrderRepository;

	@Autowired
	RazorpayVirtualAccountService razorpayVirtualAccountService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ReturnOrCancelOrderRepository returnOrCancelOrderRepository;

	@Autowired
	ReturnOrCancelOrderProductRepository returnOrCancelOrderProductRepository;

	@Autowired
	ReturnOrderStatusRepository returnOrderStatusRepository;

	@Autowired
	PlaceOrderProductRepository placeOrderProductRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	OrderStatusRepository orderStatusRepository;

	@Autowired
	LogService logService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnOrCancelOrder returnOrder(ReturnOrCancelOrderRequest returnOrCancelOrderRequest) {
		if (AppUtility.isEmpty(returnOrCancelOrderRequest)) {
			throw new GenricException("Return Order Objcet is null");
		}

		if (!AppUtility.isEmpty(returnOrCancelOrderRepository
				.findReturnOrCancelOrderByOrderId(returnOrCancelOrderRequest.getOrderId()))) {
			throw new GenricException("Order Already return");
		}
		PlaceOrder placeOrder = placeOrderRepository.findPlaceOrderById(returnOrCancelOrderRequest.getOrderId());
		if (AppUtility.isEmpty(placeOrder)) {
			throw new GenricException("Wrong order");
		}

		User user = userRepository.findUserById(returnOrCancelOrderRequest.getUserId());
		if (AppUtility.isEmpty(placeOrder)) {
			throw new GenricException("Wrong user");
		}

		ReturnOrCancelOrder rco = new ReturnOrCancelOrder();
		try {
			rco.setPlaceOrder(placeOrder);
			rco.setStatus(AppConstants.RETURN);
			rco.setUser(user);
			returnOrCancelOrderRepository.save(rco);
			List<PlaceOrderProduct> placeOrderPorduct = placeOrderProductRepository
					.findbyPlaceOrderProductByOrderId(returnOrCancelOrderRequest.getOrderId());
			Set<ReturnOrCancelOrderProduct> returnProductList = new HashSet<>();
			List<Long> ids = new ArrayList<>();
			for (Iterator<ReturnOrCancelOrderProductRequest> iterator = returnOrCancelOrderRequest.getReturnProduct()
					.iterator(); iterator.hasNext();) {
				ReturnOrCancelOrderProduct rp = new ReturnOrCancelOrderProduct();
				ReturnOrCancelOrderProductRequest obj = iterator.next();
				PlaceOrderProduct pop = placeOrderPorduct.stream()
						.filter(e -> e.getProduct().getId().equals(obj.getProductId())).collect(Collectors.toList()).get(0);
				if (AppUtility.isEmpty(pop)) {
					throw new GenricException("Product Not Available");
				}
				ids.add(obj.getProductId());
				rp.setDescription(obj.getDescription());
				rp.setProduct(pop.getProduct());
				rp.setReturnOrCancelOrder(rco);
				rp.setPrice(pop.getPrice());
				rp.setShippingCharge(pop.getShippingCharge());
				rp.setQty(obj.getQty());
				returnProductList.add(rp);
			}
			returnOrCancelOrderProductRepository.saveAll(returnProductList);
			updatePlaceOrderStatusByIds(AppConstants.RETURN, ids);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
			logService.saveErrorLog("Order not return due to error", "ReturnOrderController", "returnOrder",
					ex.getMessage());
			throw new GenricException("Order not return due to error:-" + ex.getMessage());
		}
		ReturnOrCancelOrder roco = returnOrCancelOrderRepository.findReturnOrCancelOrderById(rco.getId());
		try {
			ReturnOrderStatus rts = new ReturnOrderStatus();
			rts.setDescription("Return has processed");
			rts.setReturnOrder(roco);
			rts.setStatusMessage(AppConstants.RETURN);
			returnOrderStatusRepository.save(rts);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			logService.saveErrorLog("Error in saving return order status", "ReturnOrderController", "returnOrder",
					ex.getMessage());
		}
		return roco;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnOrCancelOrder cancelOrder(Long orderId) {
		if (orderId <= 0) {
			throw new GenricException("Order id is null");
		}

		PlaceOrder placeOrder = placeOrderRepository.findPlaceOrderById(orderId);
		if (AppUtility.isEmpty(placeOrder)) {
			throw new GenricException("Wrong order");
		}
		if (!AppUtility.isEmpty(returnOrCancelOrderRepository.findReturnOrCancelOrderByOrderId(orderId))) {
			throw new GenricException("Order Already return");
		}
		List<PlaceOrderProduct> placeOrderProduct = placeOrderProductRepository
				.findbyPlaceOrderProductByOrderId(orderId);
		if (AppUtility.isEmpty(placeOrderProduct)) {
			throw new GenricException("Product Not exist");
		}

		ReturnOrCancelOrder rco = new ReturnOrCancelOrder();
		try {
			rco.setPlaceOrder(placeOrder);
			rco.setStatus(AppConstants.CANCEL);
			rco.setUser(placeOrder.getUser());
			rco = returnOrCancelOrderRepository.save(rco);
			Set<ReturnOrCancelOrderProduct> returnProduct = new HashSet<>();
			for (Iterator<PlaceOrderProduct> iterator = placeOrderProduct.iterator(); iterator.hasNext();) {
				PlaceOrderProduct placeOrderProduct2 = (PlaceOrderProduct) iterator.next();
				ReturnOrCancelOrderProduct returnOrderProduct = new ReturnOrCancelOrderProduct();
				returnOrderProduct.setDescription("Order Cancel");
				returnOrderProduct.setProduct(placeOrderProduct2.getProduct());
				returnOrderProduct.setQty(placeOrderProduct2.getQty());
				returnOrderProduct.setPrice(placeOrderProduct2.getPrice());
				returnOrderProduct.setShippingCharge(placeOrder.getShippingCharge());
				returnOrderProduct.setReturnOrCancelOrder(rco);
				returnProduct.add(returnOrderProduct);
				quantityUpdate(placeOrderProduct);

			}
			returnOrCancelOrderProductRepository.saveAll(returnProduct);
			placeOrderRepository.updatePlaceOrderStatus(AppConstants.CANCEL, placeOrder.getId());
			OrderStatus order = new OrderStatus();
			order.setDescription("Order Cancel");
			order.setNotificationSend(false);
			order.setStatusMessage(AppConstants.CANCEL);
			order.setPlaceOrder(placeOrder);
			orderStatusRepository.save(order);
			updatePlaceOrderStatus(AppConstants.CANCEL, orderId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
			logService.saveErrorLog("error on canceling the order", "AddToCartController", "getCardDetailByUser",
					ex.getMessage());
			throw new GenricException("error on canceling the order:-" + ex.getMessage());

		}
		return returnOrCancelOrderRepository.findReturnOrCancelOrderById(rco.getId());
	}

	@Override
	public List<ReturnOrCancelOrder> findByUserId(Long userId) {
		return returnOrCancelOrderRepository.findReturnOrCancelOrderByUserIdAndStatus(userId,"Cancel");
	}

	@Override
	public ReturnOrCancelOrder findByReturnId(Long id) {
		return returnOrCancelOrderRepository.findReturnOrCancelOrderById(id);
	}

	@Override
	public PageImpl<ReturnOrCancelOrder> findAll(Pageable pageable, ReturnOrderFilter returnOrderFilter)
			throws ParseException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ReturnOrCancelOrder> criteria = builder.createQuery(ReturnOrCancelOrder.class);
		Root<ReturnOrCancelOrder> root = criteria.from(ReturnOrCancelOrder.class);
		List<ReturnOrCancelOrder> workEffortMaps = null;
		long count;
		Page<ReturnOrCancelOrder> page;
		if (AppUtility.isEmpty(returnOrderFilter)) {
			page = returnOrCancelOrderRepository.findAllReturnOrCancelOrder(pageable);
			for(int i=0; i<page.getContent().size(); i++) {
				page.getContent().get(i).getUser().setUserAddresses(null);
//				page.getContent().get(i).getPlaceOrder().setPlaceOrderProduct(null);
			}
			workEffortMaps = (List<ReturnOrCancelOrder>) page.getContent();
			count = page.getTotalElements();
		} else {
			List<Predicate> predicates = setAdvanceSeachForProduct(builder, root, returnOrderFilter);
			criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);
			workEffortMaps = entityManager.createQuery(criteria)
					.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
					.setMaxResults(pageable.getPageSize()).getResultList();
			count = entityManager.createQuery(criteria).getResultList().size();
		}
		return new PageImpl<>(workEffortMaps, pageable, count);
	}

	private List<Predicate> setAdvanceSeachForProduct(CriteriaBuilder builder, Root<ReturnOrCancelOrder> root,
			ReturnOrderFilter returnOrderFilter) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
//		if (!AppUtility.isEmpty(productFilter.getProductName()))
//			predicates.add(builder.like(builder.lower(root.get("productName")),
//					"%" + productFilter.getProductName().toLowerCase() + "%"));
//
//		if (!AppUtility.isEmpty(productFilter.getDisplayName()))
//			predicates.add(builder.like(builder.lower(root.get("displayName")),
//					"%" + productFilter.getDisplayName().toLowerCase() + "%"));
//
//		if (!AppUtility.isEmpty(productFilter.getDescription()))
//			predicates.add(builder.like(builder.lower(root.get("description")),
//					"%" + productFilter.getDescription().toLowerCase() + "%"));
//
//		if (!AppUtility.isEmpty(productFilter.getPrice()) && productFilter.getPrice() > 0)
//			predicates.add(builder.equal(root.get("price"), productFilter.getPrice()));
//
//		if (!AppUtility.isEmpty(productFilter.getDiscountPrice()) && productFilter.getDiscountPrice() > 0)
//			predicates.add(builder.equal(root.get("discountPrice"), productFilter.getDiscountPrice()));
//
//		if (!AppUtility.isEmpty(productFilter.getQty()) && productFilter.getQty() > 0)
//			predicates.add(builder.equal(root.get("qty"), productFilter.getQty()));
//		
//		if (!AppUtility.isEmpty(productFilter.getBrandId()) && productFilter.getBrandId() > 0)
//			predicates.add(builder.equal(brandJoin.get("id"), productFilter.getBrandId()));
//		
//		if (!AppUtility.isEmpty(productFilter.getCategoryId()) && productFilter.getCategoryId() > 0)
//			predicates.add(builder.equal(categoryJoin.get("id"), productFilter.getCategoryId()));
//		
//		if (!AppUtility.isEmpty(productFilter.getSubCategoryId()) && productFilter.getSubCategoryId() > 0)
//			predicates.add(builder.equal(subCategoryJoin.get("id"), productFilter.getSubCategoryId()));
//		
//		if (!AppUtility.isEmpty(productFilter.getSubCategoryName()))
//			predicates.add(builder.like(builder.lower(subCategoryJoin.get("name")),
//					"%" + productFilter.getSubCategoryName().toLowerCase() + "%"));
//		
//		if (!AppUtility.isEmpty(productFilter.getCategoryName()))
//			predicates.add(builder.like(builder.lower(categoryJoin.get("name")),
//					"%" + productFilter.getCategoryName().toLowerCase() + "%"));

		return predicates;
	}

	@Override
	public List<ReturnOrCancelOrderProduct> findReturnOrCancelOrderProductByReturnId(Long id) {
		return returnOrCancelOrderProductRepository.findReturnOrCancelOrderProductByReturnOrderId(id);
	}

	@Override
	public String getOrderStatus(String status, Long key) {
		List<String> values = Arrays.asList(AppConstants.RETURN,AppConstants.RETURN_ORDER_INITIATED, AppConstants.RETURN_ORDER_RECIEVED, AppConstants.RETURN_PAYMENT_INITIATED, AppConstants.RETURN_PAYMENT_SUCCESSFULLY);
		Map<String, Integer> map = new HashMap<>();
		int i = 0;
		for (String o : values) {
			map.put(o.toString(), i);
			i++;
		}
		ReturnOrCancelOrder  returnOrCancelOrder = returnOrCancelOrderRepository.findReturnOrCancelOrderByOrderId(key).get(0);
		Long id = returnOrCancelOrder.getId();
		ReturnOrderStatus  returnOrderStatus = returnOrderStatusRepository.getReturnOrderStatusByReturnorderId(id).get(0);
		String currentOrderStatus1 = returnOrderStatus.getStatusMessage();

		if (map.get(status) > map.get(currentOrderStatus1)){
			returnOrderStatus.setStatusMessage(status);
			returnOrCancelOrder.setStatus(status);
			this.returnOrderStatusRepository.save(returnOrderStatus);
			returnOrCancelOrder = this.returnOrCancelOrderRepository.save(returnOrCancelOrder);

			if(returnOrCancelOrder.getStatus()==AppConstants.RETURN_PAYMENT_INITIATED){
				Long userId = returnOrCancelOrder.getUser().getId();
				Long orderId = returnOrCancelOrder.getPlaceOrder().getId();
				int payment = (int)(returnOrCancelOrder.getPlaceOrder().getPayedAmount()*100);
				String paymentId = returnOrCancelOrder.getPlaceOrder().getPaymentId();
				Long refundId = returnOrCancelOrder.getId();
				razorpayVirtualAccountService.refundAmount(paymentId, userId, orderId,payment,refundId);
			}

			return "status successfully changed";
		}
		return "status not changed";

	}

	public void updatePlaceOrderStatus(String status, Long orderId) {
		placeOrderProductRepository.updatePlaceOrderProductStatus(status, orderId);
	}

	public void updatePlaceOrderStatusByIds(String status, List<Long> orderId) {
		placeOrderProductRepository.updatePlaceOrderProductStatusByIds(status, orderId);
	}
	public void quantityUpdate(List<PlaceOrderProduct> placeOrderProduct){

			Product product = productRepository.findProductById(placeOrderProduct.get(0).getProduct().getId());
				product.setQty(product.getQty() + placeOrderProduct.get(0).getQty());
				productRepository.save(product);
				log.info("the quantity is added to the main stock");
		}
}


