package com.divergent.mahavikreta.constants;

import java.util.Arrays;
import java.util.List;

/**
 * This class hold all Application constants.
 * 
 * @author Aakash
 *
 */
public class AppConstants {

	private AppConstants() {

	}

	protected static final String[] ROLES = { "ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_PRODUCER", "ROLE_TRANSPORTER", "ROLE_DISTRIBUTOR", "ROLE_RETAILER", "ROLE_USER" };
	public static final String USERNAME = "username";
	public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
	public static final String PRIVILEGES = "privileges";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String WEB_USER = "WEB_USER";


	public static final List<String> PRODUCT_INFO=  Arrays.asList("PRODUCT NAME","CAPTION" ,"TECHNICAL NAME","PACKING","PRODUCT CODE","HSN CODE","MRP", "AVAILABLE STOCK","OUR PRICE INCL GST","DESCRIPTION","COMPANY NAME","CATEGORY NAME","CATEGORY CODE","SUB CATEGORY NAME","SUB CATEGORY CODE","MINIMUM ORDER QUANTITY","SPECIFICATION", "CHILD CATEGORY", "SHIPPING CHARGE", "MARGIN");

	public static final String WHATS_APP_SINGLE_URL="https://api.gupshup.io/sm/api/v1/msg";

	public static final String RAZORPAY_VIRTUAL_ACCOUNT = "https://api.razorpay.com/v1/virtual_accounts";
	public static final String RAZORPAY_CUSTOMER_ACCOUNT = "https://api.razorpay.com/v1/customers";


	protected static final String[] ORDER_STATUS = { "PAYMENT_PENDING", "PLACED", "PACKED", "DELIVERED", "ARRIVED", "ERROR", "CANCEL","OTHER" };

	public static final String PAYMENT_PENDING = "Payment Pending";
	public static final String PAYMENT_SUCCESS = "Payment Success";
	public static final String PLACED = "Placed";
	public static final String IN_TRANSIT = "Intransit";
	public static final String PACKED = "Packed";
	public static final String DELIVERED = "Delivered";
	public static final String ARRIVED = "Arrived";
	public static final String ERROR = "Error";
	public static final String CANCEL = "Cancel";
	public static final String OTHER = "Other";
	public static final String RETURN = "Return";
	public static final String REFUND_PROCESSED = "Refund Processed";
	public static final String BULK_ORDER = "bulkOrder";
	public static final String RETURN_ORDER_RECIEVED = "Return order received from user";
	public static final String RETURN_ORDER_INITIATED = "Return order initiated";
	public static final String RETURN_PAYMENT_INITIATED = "Return payment initiated";
	public static final String RETURN_PAYMENT_SUCCESSFULLY = "Return payment successfully";


	public static final String YELLOW_MESSENGER_CURL = "https://app.yellowmessenger.com/integrations/whatsapp/send?bot=";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String X_AUTH_TOKEN = "x-auth-token";
	public static final String COOKIE = "Cookie";
	public static final String POLICY = "policy";
	public static final String CODE = "code";
	public static final String DEFAULT = "default";
	public static final String NAMESPACE = "namespace";
	public static final String ELEMENT_NAME = "element_name";
	public static final String LANGUAGE = "language";
	public static final String LOCALIZABLE_PARAMS = "localizable_params";
	public static final String ISD_CODE = "91";
	public static final String TO = "to";
	public static final String TTL = "ttl";
	public static final String TYPE = "type";
	public static final String HSM = "hsm";
	public static final String BODY = "body";
	public static final String TEXT = "text";
	public static final String TEMPLATE = "template";
	public static final String MEDIA_NOTIFICATION = "media-notification";
	public static final String COMPONENTS = "components";
	public static final String PARAMETERS = "parameters";
	public static final String NAME = "name";
}
