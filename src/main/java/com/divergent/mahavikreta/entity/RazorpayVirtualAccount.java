package com.divergent.mahavikreta.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rzpy_virtual_acc")
public class RazorpayVirtualAccount {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "virtual_account_id")
	private String virtualAccountId;

	@Column(name = "name")
	private String name;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "ifsc")
	private String ifsc;

	@Column(name = "entity")
	private String entity;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "amount_paid",columnDefinition="Decimal(10,2) default '0.00'")
	private double amountPaid;

	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

}
