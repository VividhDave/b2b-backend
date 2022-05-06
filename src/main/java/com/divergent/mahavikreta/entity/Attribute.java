package com.divergent.mahavikreta.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

@Getter
@Setter
@Entity
@Table(name = "attribute")
public class Attribute extends DateAudit implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "key_value")
	private String key;
	
	@Column(name = "required")
	private boolean required;
	
	@Column(name = "type")
	private String type;

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
	@JoinColumn(name = "category_id", nullable = true)
	@JsonIgnoreProperties(value = {"attribute", "hibernateLazyInitializer"})
	private Category category;
	
//	@OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
//    private Set<ProductAttribute> productAttribute = new HashSet<>();
//
//	public Set<ProductAttribute> getProductAttribute() {
//		return productAttribute;
//	}
//
//	public void setProductAttribute(Set<ProductAttribute> productAttribute) {
//		this.productAttribute = productAttribute;
//	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	
}

