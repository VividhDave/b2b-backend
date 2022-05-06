package com.divergent.mahavikreta.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.divergent.mahavikreta.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "return_cancel_product")
public class ReturnOrCancelOrderProduct extends DateAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "description", length = 2000)
	private String description;

	@Column(name = "qty")
	private int qty;

	@Column(name = "price", columnDefinition = "Decimal(10,2) default '0.00'")
	private double price;

	@Column(name = "shipping_charge",columnDefinition = "Decimal(10,2) default '0.00'")
	private double shippingCharge;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Product product;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "return_product", nullable = false)
	@JsonIgnoreProperties(value = { "returnProduct", "hibernateLazyInitializer" })
	private ReturnOrCancelOrder returnOrCancelOrder;

}
