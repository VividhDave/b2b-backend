package com.divergent.mahavikreta.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.divergent.mahavikreta.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends DateAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "technical_name")
	private String technicalName;
	
	@Column(name = "product_code")
	private String productCode;
	
	@Column(name = "hsn_code",columnDefinition="Decimal(10,0) default 0")
	private double hsnCode;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "description",length = 2000)
	private String description;

	@Column(name = "price",columnDefinition="Decimal(10,2) default '0.00'")
	private double price;
	
	@Column(name = "packaging")
	private String packaging;
	
	@Column(name = "discountPrice",columnDefinition="Decimal(10,2) default '0.00'")
	private double discountPrice;
	
	@Column(name = "shipping_charge",columnDefinition="Decimal(10,2) default '0.00'")
	private double shippingCharge;
	
	@Column(name = "qty")
	private int qty;

	@Column(name = "specification",length = 4000)
	private String specification;

	@Column(name = "margin")
	private int margin;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "brand_id", nullable = false)
	@JsonIgnoreProperties(value = { "product", "hibernateLazyInitializer" })
	private Brand brand;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	@JsonIgnoreProperties(ignoreUnknown = true, value = { "subCategory", "hibernateLazyInitializer" })
	private Category category;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "sub_category_id", nullable = false)
	@JsonIgnoreProperties(ignoreUnknown = true, value = { "category", "hibernateLazyInitializer" })
	private SubCategory subCategory;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "chilled_category_id", nullable = true)
	@JsonIgnoreProperties(ignoreUnknown = true, value = { "subCategory", "hibernateLazyInitializer" })
	private ChilledCategory chilledCategory;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//	@JsonIgnoreProperties(ignoreUnknown = true, value = { "attribute", "hibernateLazyInitializer" })
	private Set<ProductAttribute> productAttribute;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnoreProperties(ignoreUnknown = true, value = { "product", "hibernateLazyInitializer" })
	private Set<ProductImage> productImage;
	

}
