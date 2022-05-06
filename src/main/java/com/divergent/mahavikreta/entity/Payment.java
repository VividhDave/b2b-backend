package com.divergent.mahavikreta.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.divergent.mahavikreta.audit.DateAudit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_details")
public class Payment  extends DateAudit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "entity")
	private String entity;
	
	@Column(name = "transactionId")
	private String transactionId;
	
	@Column(name = "amount",columnDefinition="Decimal(10,2) default '0.00'")
	private double amount;
	
	@Column(name = "currency")
    private String currency;
	
	@Column(name = "base_amount",columnDefinition="Decimal(10,2) default '0.00'")
	private double baseAmount;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "rzpay_order_id")
	private String orderId;
	
	@Column(name = "rzpay_invoice_id")
	private String invoiceId;
	
	@Column(name = "international")
	private boolean international;
	
	@Column(name = "method")
	private String method;
	
	@Column(name = "amount_refunded",columnDefinition="Decimal(10,2) default '0.00'")
	private double amountRefunded;
	
	@Column(name = "amount_transferred")
	private double amountTransferred;
	
	@Column(name = "refund_status")
	private String refundStatus;
	
	@Column(name = "captured")
	private boolean captured;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "card_id")
	private String cardId;
	
	@Column(name = "bank")
	private String bank;
	
	@Column(name = "wallet")
	private String wallet;
	
	@Column(name = "vpa")
	private String vpa;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "contact")
	private String contact;
	
	@Column(name = "fee")
	private String fee;
	
	@Column(name = "tax")
	private String tax;
	
	@Column(name = "error_code")
	private String errorCode;
	
	@Column(name = "error_description")
	private String errorDescription;
	
	@Column(name = "error_source")
	private String errorSource;
	
	@Column(name = "error_step")
	private String errorStep;
	
	@Column(name = "error_reason")
	private String errorReason;
	
	@Column(name = "account_id")
	private String accountId;
	
	@Column(name = "event")
	private String event;
	
	@Column(name = "refund_payment_id")
	private String rePaymentId;
	
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "customer_id")
	private String customerId;
}
