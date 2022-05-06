
package com.divergent.mahavikreta.service.impl;

public class ReturnOrderServiceImpl{

//
//	@Autowired
//	private ProductRepository productRepository;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private PlaceOrderRepository placeOrderRepositiry;
//
//	@Autowired
//	private OrderStatusRepository orderStatusRepository;
//	
//	@PersistenceContext
//	private EntityManager entityManager;
//
//	@Transactional(rollbackFor = Exception.class)
//	public ReturnOrder returnOrder(ReturnProductRequest returnProductRequest) {
//		ReturnOrder returnOrder = new ReturnOrder();
//
//		OrderStatus orderStatus = orderStatusRepository
//				.findOrderStatusByOrderIdAndStatus(returnProductRequest.getOrderId(), AppConstants.DELIVERED);
//		if (AppUtility.isEmpty(orderStatus)) {
//			throw new GenricException("Order Not Delivered");
//		}
//		
//		TimeUnit time = TimeUnit.DAYS;
//		long difference = time.convert(new Date().getTime() - orderStatus.getCreatedDate().getTime(),
//				TimeUnit.MILLISECONDS);
//
//		if (difference < 7) {
//			returnOrder.setQty(returnProductRequest.getQty());
//			returnOrder.setDescription(returnProductRequest.getDescription());
//			Product prod = productRepository.findProductById(returnProductRequest.getProductId());
//
//			if (AppUtility.isEmpty(prod)) {
//				throw new GenricException("Product Not Exist");
//			}
//			returnOrder.setProduct(prod);
//			User user = userRepository.findUserById(returnProductRequest.getUserId());
//
//			if (AppUtility.isEmpty(user)) {
//				throw new GenricException("User Not Exist");
//			}
//			returnOrder.setUser(user);
//
//			PlaceOrder placeOrder = placeOrderRepositiry.findPlaceOrderById(returnProductRequest.getOrderId());
//
//			if (AppUtility.isEmpty(placeOrder)) {
//				throw new GenricException("Order Not Exist");
//			}
//			if(AppUtility.isEmpty(placeOrder.getPaymentId())) {
//				throw new GenricException("Payment not done");
//			}
//			returnOrder.setPlaceOrder(placeOrder);
//		} else {
//			throw new GenricException("You can not return Product after 7 Days");
//		}
//		return returnOrderRepository.save(returnOrder);
//
//	}
//
//	@Override
//	public PageImpl<ReturnOrder> getList(Pageable pageable, ReturnOrderFilter returnOrderFilter) throws ParseException {
//			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//
//			CriteriaQuery<Tuple> criteria = builder.createTupleQuery();
//
//			Root<ReturnOrder> root = criteria.from(ReturnOrder.class);
//     		Join<ReturnOrder, PlaceOrder> placeOrder = root.join("placeOrder", JoinType.LEFT);
//     		Join<ReturnOrder, User> userJoin = root.join("user", JoinType.LEFT);
//			
//			List<ReturnOrder> workEffortMaps = null;
//			long count;
//			Page<ReturnOrder> page;
//			if (AppUtility.isEmpty(returnOrderFilter)) {
//				page = returnOrderRepository.findAllReturnOrder(pageable);
//				workEffortMaps = (List<ReturnOrder>) page.getContent();
//				count = page.getTotalElements();
//			} else {
//				criteria.multiselect(placeOrder.get("id"), placeOrder.get("discount"), placeOrder.get("orderDate"),
//						placeOrder.get("paymentId"), placeOrder.get("status"), placeOrder.get("promoCode"),
//						placeOrder.get("totalSaleAmount"));
//
//				List<Predicate> predicates = setAdvanceSeachForReturnOrder(builder, root,placeOrder,userJoin, returnOrderFilter);
//
//				criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);
//
//				List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
//						.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
//						.setMaxResults(pageable.getPageSize()).getResultList();
//				workEffortMaps = new ArrayList<>();
//				for (Tuple tuple : workEffortTuples) {
//					ReturnOrder returnOrder = new ReturnOrder();
//					PlaceOrder po = new PlaceOrder();
//					po.setId((Long) tuple.get(0));
//					po.setDiscount((Long) tuple.get(1));
//					po.setOrderDate((Instant) tuple.get(2));
//					po.setPaymentId((String) tuple.get(3));
//					po.setStatus((String) tuple.get(4));
//					po.setPromoCode((String) tuple.get(4));
//					po.setTotalSaleAmount((double) tuple.get(5));
//					
//					workEffortMaps.add(returnOrder);
//				}
//				count = entityManager.createQuery(criteria).getResultList().size();
//			}
//
//			return new PageImpl<>(workEffortMaps, pageable, count);
//		}
//
//		private List<Predicate> setAdvanceSeachForReturnOrder(CriteriaBuilder builder, Root<ReturnOrder> root,Join<ReturnOrder,
//				PlaceOrder> placeOrder, Join<ReturnOrder, User> userJoin,ReturnOrderFilter returnOrderFilter) throws ParseException {
//			List<Predicate> predicates = new ArrayList<>();
//			if (!AppUtility.isEmpty(returnOrderFilter.getPromoCode()))
//				predicates.add(builder.like(builder.lower(placeOrder.get("promoCode")),
//						"%" + returnOrderFilter.getPromoCode().toLowerCase() + "%"));
//
//			if (!AppUtility.isEmpty(returnOrderFilter.getRemark()))
//				predicates.add(builder.like(builder.lower(placeOrder.get("remark")),
//						"%" + returnOrderFilter.getRemark().toLowerCase() + "%"));
//
//			if (!AppUtility.isEmpty(returnOrderFilter.getStatus()))
//				predicates.add(builder.like(builder.lower(placeOrder.get("status")),
//						"%" + returnOrderFilter.getStatus().toLowerCase() + "%"));
//
//			if (!AppUtility.isEmpty(returnOrderFilter.getDiscount()) && returnOrderFilter.getDiscount() > 0)
//				predicates.add(builder.equal(placeOrder.get("discount"), returnOrderFilter.getDiscount()));
//
//			if (!AppUtility.isEmpty(returnOrderFilter.getUserId()) && returnOrderFilter.getUserId() > 0)
//				predicates.add(builder.equal(userJoin.get("id"), returnOrderFilter.getUserId()));
//
//			if (!AppUtility.isEmpty(returnOrderFilter.getOrderDate()))
//				predicates.add(builder.equal(placeOrder.get("orderDate"), returnOrderFilter.getOrderDate()));
//
//			return predicates;
//		}
//
//		@Override
//		public List<ReturnOrder> getReturnOrderProductListByOrderId(Long orderId) {
//			return returnOrderRepository.findReturnOrderByOrderId(orderId);
//		}
}
