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
@Table(name = "refund_payment_details")
public class RefundPayment extends DateAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "entity")
	private String entity;

	@Column(name = "amount")
	private String amount;

	@Column(name = "refund_id")
	private String refundId;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "status")
	private String status;
}
