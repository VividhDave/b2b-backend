package com.divergent.mahavikreta.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.entity.*;
import com.divergent.mahavikreta.entity.filter.PaymentFilter;
import com.divergent.mahavikreta.repository.*;
import com.divergent.mahavikreta.utility.YellowMessengerAPIIntegration;
import com.divergent.mahavikreta.entity.filter.PaymentFilter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.WebhookRequestService;
import com.divergent.mahavikreta.utility.AppUtility;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebhookRequestServiceImpl implements WebhookRequestService {

	@Value("${razorpay.webhook.secret}")
	public String secret;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	PlaceOrderRepository placeOrderRepository;

	@Autowired
	PlaceOrderProductRepository placeOrderProductRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	RazorpayVirtualAccountRepository razorpayVirtualAccountRepository;

	@Autowired
	OrderStatusRepository orderStatusRepository;

	@Autowired
	LogService logService;

	@Autowired
	UserRepository userRepository;

	@Override
	public boolean verifyPayment(String body, HttpHeaders headers) {
		boolean signaturverify = true;
		//			signaturverify = Utils.verifyWebhookSignature(body, headers.getFirst("x-razorpay-signature"), secret);
		if (signaturverify) {
			log.info("signature verifyed");
			validateAndSavePayment(body);
			log.info("process compleate");
		} else {
			log.info("signature is wrong " + headers.getFirst("x-razorpay-signature"));
		}

		return signaturverify;
	}

	public void validateAndSavePayment(String body) {
		try {
			log.error(body);
			Payment payment = new Payment();
			JSONObject json = new JSONObject(body);
			payment.setEvent(AppUtility.checkNullKey(json, "event"));
			payment.setAccountId(AppUtility.checkNullKey(json, "account_id"));
			JSONObject payload = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
			String virtualAccount = AppUtility.checkNullKey(payload, "customer_id");
			payment.setTransactionId(AppUtility.checkNullKey(payload, "id"));
			payment.setEntity(AppUtility.checkNullKey(payload, "entity"));
			payment.setAmount(AppUtility.checkDoubleNullKey(payload, "amount"));
			payment.setCurrency(AppUtility.checkNullKey(payload, "currency"));
//			payment.setBaseAmount(AppUtility.checkDoubleNullKey(payload, "base_amount"));
			payment.setStatus(AppUtility.checkNullKey(payload, "status"));
			payment.setOrderId(AppUtility.checkNullKey(payload, "order_id"));
			payment.setInvoiceId(AppUtility.checkNullKey(payload, "invoice_id"));
			payment.setInternational(AppUtility.checkBooleanNullKey(payload, "international"));
			payment.setMethod(AppUtility.checkNullKey(payload, "method"));
			payment.setAmount(AppUtility.checkDoubleNullKey(payload, "amount_refunded"));
//			payment.setAmountTransferred(AppUtility.checkDoubleNullKey(payload, "amount_transferred"));
			payment.setRefundStatus(AppUtility.checkNullKey(payload, "refund_status"));
			payment.setCaptured(AppUtility.checkBooleanNullKey(payload, "captured"));
			payment.setDescription(AppUtility.checkNullKey(payload, "description"));
			payment.setCardId(AppUtility.checkNullKey(payload, "card_id"));
			payment.setBank(AppUtility.checkNullKey(payload, "bank"));
			payment.setWallet(AppUtility.checkNullKey(payload, "wallet"));
			payment.setVpa(AppUtility.checkNullKey(payload, "vpa"));
			payment.setEmail(AppUtility.checkNullKey(payload, "email"));
			payment.setContact(AppUtility.checkNullKey(payload, "contact"));
			payment.setFee(AppUtility.checkNullKey(payload, "fee"));
			payment.setTax(AppUtility.checkNullKey(payload, "tax"));
			payment.setErrorCode(AppUtility.checkNullKey(payload, "error_code"));
			payment.setErrorDescription(AppUtility.checkNullKey(payload, "error_description"));
			payment.setErrorSource(AppUtility.checkNullKey(payload, "error_source"));
			payment.setErrorStep(AppUtility.checkNullKey(payload, "error_step"));
			payment.setErrorReason(AppUtility.checkNullKey(payload, "error_reason"));
			payment.setCustomerId(AppUtility.checkNullKey(payload, "customer_id"));
			if (AppUtility.checkNullKey(payload, "entity").equals("refund")) {
				payment.setRePaymentId(AppUtility.checkNullKey(payload, "payment_id"));
			}
			if (payment.getAmount() > 0) {
				log.info("Payment type double");
				payment.setAmount((payment.getAmount() / 100));
			} else {
				payment.setAmount(AppUtility.checkFlotNullKey(payload, "amount"));
//				payment.setBaseAmount(AppUtility.checkFlotNullKey(payload, "base_amount"));
				if (payment.getAmount() > 0) {
					log.info("Payment type flot");
					payment.setAmount((payment.getAmount() / 100));
				} else {
					payment.setAmount(AppUtility.checkIntNullKey(payload, "amount"));
//					payment.setBaseAmount(AppUtility.checkIntNullKey(payload, "base_amount"));
					if (payment.getAmount() > 0) {
						log.info("Payment type int");
						payment.setAmount((payment.getAmount() / 100));
					}
				}
			}
			Payment paymentObj = paymentRepository.save(payment);
			log.info("payment info saved");
			try {
			if (!AppUtility.checkNullKey(payload, "entity").equals("refund")) {
				RazorpayVirtualAccount rzpay = razorpayVirtualAccountRepository
						.getRazrVirtualAcByCustId(virtualAccount);
				if (!AppUtility.isEmpty(rzpay)) {
					Long id = rzpay.getUser().getId();
					log.info("user Id:-" + id);
					List<PlaceOrder> ls = placeOrderRepository.findPlaceOrderByUserId(id, AppConstants.PAYMENT_PENDING);
					log.info("List of place order:-" + ls);
					log.info("List of place order:-" + ls.size());
					if (ls.size() == 1) {
						PlaceOrder placeOrder = ls.get(0);
						placeOrder.setPaymentId(paymentObj.getTransactionId());
						if (placeOrder.getDueAmount() > 0) {
							if (paymentObj.getAmount() >= placeOrder.getDueAmount()) {
								log.info("success due amount");
								placeOrder.setPayed(true);
								placeOrder.setDueAmount(placeOrder.getDueAmount()-paymentObj.getAmount());
								placeOrder.setPayedAmount(placeOrder.getPayedAmount() + paymentObj.getAmount());
								placeOrder.setStatus(AppConstants.PAYMENT_SUCCESS);
								saveOrderPaymentStatus(AppConstants.PAYMENT_SUCCESS, placeOrder, "Payment Success");
								saveOrderProductPaymentStatus(AppConstants.PAYMENT_SUCCESS, placeOrder);
								quantityUpdate(placeOrder);

							} else {
								placeOrder.setPayedAmount(placeOrder.getPayedAmount() + paymentObj.getAmount());
								placeOrder.setDueAmount(placeOrder.getDueAmount() - paymentObj.getAmount());
								placeOrder.setPayed(false);
								log.info("remaining due amount");
								saveOrderPaymentStatus(AppConstants.PAYMENT_PENDING, placeOrder,
										"Payment Partialy Success please do remaining payment");
							}
						} else {
							if (paymentObj.getAmount() >= placeOrder.getTotalSaleAmount()) {
								log.info("Payment success with full amount");
								placeOrder.setPayed(true);
								placeOrder.setPayedAmount(paymentObj.getAmount());
								placeOrder.setStatus(AppConstants.PAYMENT_SUCCESS);
								saveOrderPaymentStatus(AppConstants.PAYMENT_SUCCESS, placeOrder, "Payment Success");
								saveOrderProductPaymentStatus(AppConstants.PAYMENT_SUCCESS, placeOrder);
								quantityUpdate(placeOrder);

							} else {
								log.info("Payment Partialy success with full amount");
								placeOrder.setPayedAmount(paymentObj.getAmount());
								placeOrder.setDueAmount(placeOrder.getTotalSaleAmount() - placeOrder.getPayedAmount());
								placeOrder.setPayed(false);
								saveOrderPaymentStatus(AppConstants.PAYMENT_PENDING, placeOrder,
										"Payment Partialy Success please do remaining payment");
							}
						}
						log.info("Update Place order");
						placeOrderRepository.save(placeOrder);
						User user = userRepository.findUserById(placeOrder.getUser().getId());
						if(!AppUtility.isEmpty(user.getWhatsappMobileNumber())) {
							YellowMessengerAPIIntegration.paymentConfirmationAPI(user.getWhatsappMobileNumber(), user.getFirstName(), String.valueOf(placeOrder.getTotalSaleAmount()),
									payment.getTransactionId(), String.valueOf(Instant.now()), String.valueOf(placeOrder.getProductTotalAmount()));
						}
					} else if (ls.size() > 1) {
						PlaceOrder placeOrders = null;
						for (PlaceOrder placeOrder : ls) {
							if (paymentObj.getAmount() == placeOrder.getPayedAmount()) {
								placeOrders = placeOrder;
							}
						}
						if (AppUtility.isEmpty(placeOrders)) {
							log.info("Payment initiat but no any order found in our database Payment id:-"
									+ paymentObj.getTransactionId() + ", Amount = " + payment.getAmount());
						} else {
							placeOrders.setPaymentId(paymentObj.getTransactionId());
							placeOrders.setPayed(true);
							placeOrders.setStatus(AppConstants.PAYMENT_SUCCESS);
							log.info("Update Place order 2");
							placeOrderRepository.save(placeOrders);
							User user = userRepository.findUserById(placeOrders.getUser().getId());
							if(!AppUtility.isEmpty(user.getWhatsappMobileNumber())) {

								YellowMessengerAPIIntegration.paymentConfirmationAPI(user.getWhatsappMobileNumber(), user.getFirstName(), String.valueOf(placeOrders.getTotalSaleAmount()),
										payment.getTransactionId(), String.valueOf(Instant.now()), String.valueOf(placeOrders.getProductTotalAmount()));
							}
						}
					} else {
						log.info("Payment initiat but no any order found in our database Payment id:-"
								+ paymentObj.getTransactionId() + ", Amount = " + payment.getAmount());
					}
				} else {
					log.info("Paymet saved but order not found");
				}
			}
			}catch(Exception ex) {
				logService.saveErrorLog("Payment info save but not update on order", "WebhookREquestController", "verifyPayment and Save", ex.getMessage());
			}
		} catch (Exception ex) {
			log.error("error in payment parshing entity");
			log.error(ex.getMessage());
			log.error(body);
			logService.saveErrorLog("Error saving and parshing payment details", "WebhookREquestController", "verifyPayment and Save", ex.getMessage());
		}
	}

	public void saveOrderPaymentStatus(String status, PlaceOrder placeOrder, String message) {
		try {
			log.info("Save Payment status:-" + status);
			OrderStatus order = new OrderStatus();
			order.setDescription(status);
			order.setNotificationSend(false);
			order.setStatusMessage(message);
			order.setPlaceOrder(placeOrder);
			orderStatusRepository.save(order);
			log.info("Payment status saved");
		} catch (Exception ex) {
			log.error("order status not saved:- ", ex.getMessage());
			logService.saveErrorLog("Error updateting order status at the time of payment payment", "WebhookREquestController", "verifyPayment and Save", ex.getMessage());
		}
	}

	public void saveOrderProductPaymentStatus(String status, PlaceOrder placeOrder) {
		try {
			log.info("Save order product Payment status:-" + status);
			placeOrderProductRepository.updatePlaceOrderProductStatus(status, placeOrder.getId());
			log.info("Order product Payment status saved");
		} catch (Exception ex) {
			log.error("order product status not saved:- ", ex.getMessage());
			logService.saveErrorLog("Error updateting order product status at the time of payment payment", "WebhookREquestController", "verifyPayment and Save", ex.getMessage());
		}
	}

	@Override
	public PageImpl<Map<String, String>> getList(Pageable pageable) throws ParseException {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//
//		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();
//
//		Root<User> root = criteria.from(User.class);
		List<Map<String,String>> list;
		list = paymentRepository.getAllPaymentHistory(pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize());
		Long count = paymentRepository.getCountPaymentHistory();
//		workEffortMaps = (List<Map<String, String>>) page.getContent();
//		count = page.getTotalElements();

//		} else {
//			criteria.multiselect(root.get("id"), root.get("username"),root.get("email"),root.get("phone"),
//					root.get("firstName"),root.get("lastName"),
//					root.get("gstin"),root.get("enabled"),
//					root.get("address"),root.get("city"),
//					root.get("state"),root.get("pincode"),root.get("shopName"));
//
//			List<Predicate> predicates = setAdvanceSeachForUser(builder, root, userName);
//
//			criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);
//
//			List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
//					.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
//					.setMaxResults(pageable.getPageSize()).getResultList();
//			workEffortMaps = new ArrayList<>();
//			for (Tuple tuple : workEffortTuples) {
//				User user = new User();
//				user.setId((Long) tuple.get(0));
//				user.setUsername((String) tuple.get(1));
//				user.setEmail((String) tuple.get(2));
//				user.setPhone((String) tuple.get(3));
//				user.setFirstName((String) tuple.get(4));
//				user.setLastName((String) tuple.get(5));
//				user.setGstin((String) tuple.get(6));
//				user.setEnabled((Boolean) tuple.get(7));
//				user.setAddress((String) tuple.get(8));
//				user.setCity((String) tuple.get(9));
//				user.setState((String) tuple.get(10));
//				user.setPincode((String) tuple.get(11));
//				user.setShopName((String) tuple.get(12));
//				workEffortMaps.add(user);
//			}
//			count = entityManager.createQuery(criteria).getResultList().size();
//		}
		return new PageImpl<Map<String, String>>(list, pageable, count);
	}

	@Override
	public List getPaymentListByEmail(String email) {
		return paymentRepository.getPaymentListByEmail(email);
	}

//	private List<Predicate> setAdvanceSeachForUser(CriteriaBuilder builder, Root<User> root,
//			String userName) throws ParseException {
//		List<Predicate> predicates = new ArrayList<>();
//		if (!AppUtility.isEmpty(userName))
//			predicates.add(builder.like(builder.lower(root.get("name")), "%" + userName.toLowerCase() + "%"));
//
//		return predicates;
//	}

	public void quantityUpdate(PlaceOrder placeOrder){
		for(int i=0;i<placeOrder.getPlaceOrderProduct().size();i++){
		PlaceOrderProduct placeOrderProduct =  (PlaceOrderProduct) placeOrder.getPlaceOrderProduct();
		Product product = productRepository.findProductById(placeOrderProduct.getProduct().getId());
		if(product.getQty()>placeOrderProduct.getQty()) {
			product.setQty(product.getQty() - placeOrderProduct.getQty());
			productRepository.save(product);
		}
		log.error("You cannot order this amount of Quantity because there is not enough stock");
	}}
}
