package com.divergent.mahavikreta.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.entity.RazorpayVirtualAccount;
import com.divergent.mahavikreta.entity.RefundPayment;
import com.divergent.mahavikreta.entity.ReturnOrCancelOrder;
import com.divergent.mahavikreta.entity.ReturnOrderStatus;
import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.entity.UserAddress;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.PlaceOrderRepository;
import com.divergent.mahavikreta.repository.RazorpayVirtualAccountRepository;
import com.divergent.mahavikreta.repository.RefundPaymentRepository;
import com.divergent.mahavikreta.repository.ReturnOrCancelOrderRepository;
import com.divergent.mahavikreta.repository.ReturnOrderStatusRepository;
import com.divergent.mahavikreta.repository.UserRepository;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.RazorpayVirtualAccountService;
import com.divergent.mahavikreta.utility.AppUtility;
import com.razorpay.Customer;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;
import com.razorpay.VirtualAccount;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RazorpayVirtualAccountServiceImpl implements RazorpayVirtualAccountService {

	@Autowired
	RazorpayVirtualAccountRepository razorpayVirtualAccountRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RefundPaymentRepository refundPaymentRepository;

	@Autowired
	PlaceOrderRepository placeOrderRepository;

	@Autowired
	ReturnOrCancelOrderRepository returnOrCancelOrderRepository;

	@Autowired
	ReturnOrderStatusRepository returnOrderStatusRepository;

	@Autowired
	LogService logService;

	@Value("${razorpay.key}")
	public String key;

	@Value("${razorpay.secret}")
	public String secret;

	@Override
	public RazorpayVirtualAccount createVirtualAccount(Long userId) throws RazorpayException {
		User user = userRepository.findUserById(userId);
		if (AppUtility.isEmpty(user)) {
			throw new GenricException("User not exist");
		}
		if (AppUtility.isEmpty(user.getCustomerId())) {
			 user = createRazorpayCustomer(user);
		}
		VirtualAccount virtualAccount = null;
		try {
			virtualAccount = createRazorpayClient().VirtualAccounts.create(prepareVirtualAccountJsonObject(user));
		} catch (RazorpayException e) {
			e.printStackTrace();
		}
		JSONArray array = virtualAccount.get("receivers");
		JSONObject reciverObject = array.getJSONObject(0);
		if (AppUtility.isEmpty(virtualAccount)) {
			throw new GenricException("Virtual Account not created");
		}
		try {
			RazorpayVirtualAccount rz = new RazorpayVirtualAccount();
			rz.setAccountNumber(AppUtility.checkNullKey(reciverObject, "account_number"));
			rz.setVirtualAccountId(virtualAccount.get("id"));
			rz.setEntity(virtualAccount.get("entity"));
			rz.setAmountPaid(new Double((virtualAccount.get("amount_paid").toString())));
			rz.setIfsc(AppUtility.checkNullKey(reciverObject, "ifsc"));
			rz.setBankName(AppUtility.checkNullKey(reciverObject, "bank_name"));
			rz.setName(AppUtility.checkNullKey(reciverObject, "name"));
			rz.setUser(user);
			return razorpayVirtualAccountRepository.save(rz);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			logService.saveErrorLog("Proble on saving Virtual Account detail on our db", "RazorPayController",
					"createVirtualAccount", ex.getMessage());
			throw new GenricException("Proble on saving Virtual Account detail on our db");
		}
	}

	public JSONObject prepareVirtualAccountJsonObject(User user) {
		try {
		JSONObject virtualAccoutnObject = new JSONObject();
		JSONObject receivers = new JSONObject();
		List<String> types = new ArrayList<String>();
		types.add("bank_account");
		receivers.put("types", types);
		JSONObject notes = new JSONObject();
		notes.put("user_id", user.getId());
		virtualAccoutnObject.put("receivers", receivers);
		virtualAccoutnObject.put("description", "Virtual Account created for " + user.getFirstName());
		if (!AppUtility.isEmpty(user.getCustomerId())) {
			virtualAccoutnObject.put("customer_id", user.getCustomerId());
		}
		virtualAccoutnObject.put("notes", notes);
		return virtualAccoutnObject;
	} catch (Exception ex) {
		log.error(ex.getMessage());
		logService.saveErrorLog("Error on prepareing Virtual Account Json Object", "RazorpayController",
				"prepareVirtualAccountJsonObject", ex.getMessage());
		throw new GenricException("Error on prepareing Virtual Account Json Object:-" + ex.getMessage());
	}
	}

	public JSONObject prepareCustomerAccountJsonObject(User user) {
		try {
//			String phone = null;
//			Set<UserAddress> userAdd = user.getUserAddresses();
//			if (userAdd.size() > 0) {
//				UserAddress address = userAdd.iterator().next();
//				phone = address.getPhone();
//			}
			JSONObject customerAccoutnObject = new JSONObject();
			customerAccoutnObject.put("name", user.getFirstName() + " " + user.getLastName());
			customerAccoutnObject.put("contact", user.getWhatsappMobileNumber());
			customerAccoutnObject.put("email", user.getEmail());
			customerAccoutnObject.put("fail_existing", "0");
			customerAccoutnObject.put("gstin", user.getGstin());
			return customerAccoutnObject;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			logService.saveErrorLog("Error on prepareing Customer Account Json Object", "RazorpayController",
					"prepareCustomerAccountJsonObject", ex.getMessage());
			throw new GenricException("Error on prepareing Customer Account Json Object:-" + ex.getMessage());
		}
	}

	public HttpHeaders createHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "basic " + key);
		return headers;
	}

	public User createRazorpayCustomer(User user) throws RazorpayException {
		Customer customer = createRazorpayClient().Customers.create(prepareCustomerAccountJsonObject(user));
		user.setCustomerId(customer.get("id"));
		return userRepository.save(user);
	}

	@Override
	public RazorpayVirtualAccount findById(Long userId) throws JsonProcessingException, IOException, RazorpayException {
		RazorpayVirtualAccount rzpay = razorpayVirtualAccountRepository.findRazorpayVirtualAccountByUserId(userId);
		if (AppUtility.isEmpty(rzpay)) {
			rzpay = createVirtualAccount(userId);
		}
		return rzpay;
	}

	public RazorpayClient createRazorpayClient() {
		RazorpayClient razorpayClient = null;
		try {
			razorpayClient = new RazorpayClient(key, secret);
		} catch (RazorpayException e) {
			log.error(e.getMessage());
			logService.saveErrorLog("Error on Connecting razorpay client", "RazorpayController",
					"createRazorpayClient", e.getMessage());
		}

		return razorpayClient;
	}

	@Override
	public RefundPayment refundAmount(String paymentId, Long userId, Long orderId, int payment, Long returnId) {
		try {
			JSONObject refundRequest = new JSONObject();
			refundRequest.put("amount", payment);
			refundRequest.put("payment_id", paymentId);
			JSONObject note = new JSONObject();
			note.put("userId", userId.toString());
			note.put("orderId", orderId.toString());
			refundRequest.put("notes", note);
			Refund refund = createRazorpayClient().Payments.refund(refundRequest);
			RefundPayment refundPayment = new RefundPayment();
			refundPayment.setAmount(refund.get("amount"));
			refundPayment.setEntity(refund.get("entity"));
			refundPayment.setTransactionId(refund.get("payment_id"));
			refundPayment.setRefundId(refund.get("id"));
			refundPayment.setStatus(refund.get("status"));
			JSONObject notes = refund.get("notes");
			refundPayment.setUserId(AppUtility.checkNullKey(notes, "userId"));
			refundPayment.setOrderId(AppUtility.checkNullKey(notes, "orderId"));
			refundPayment = refundPaymentRepository.save(refundPayment);
			try {
				ReturnOrCancelOrder roco = returnOrCancelOrderRepository.findReturnOrCancelOrderById(returnId);
				ReturnOrderStatus rts = new ReturnOrderStatus();
				rts.setDescription("Refund has processed");
				rts.setReturnOrder(roco);
				rts.setStatusMessage(AppConstants.REFUND_PROCESSED);
				returnOrderStatusRepository.save(rts);
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex.getMessage());
				logService.saveErrorLog("Error on Saving ReturnOrCancelOrder status", "RazorpayController",
						"refundAmount", ex.getMessage());
			
			}
			return refundPayment;
		} catch (RazorpayException e) {
			log.error("error in refund payment", e);
			logService.saveErrorLog("Error on Refund Payment", "RazorpayController",
					"refundAmount", e.getMessage());
		}
		return null;
	}

}
