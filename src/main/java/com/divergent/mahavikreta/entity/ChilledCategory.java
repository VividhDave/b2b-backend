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

@Getter
@Setter
@Entity
@Table(name = "chilled_category")
public class ChilledCategory extends DateAudit implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "code")
	private String code;

	@Column(name = "status")
	private boolean status;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "sub_category_id", nullable = true)
	@JsonIgnoreProperties(value = {"category", "hibernateLazyInitializer"})
	private SubCategory subCategory;

	@Column(name = "image", columnDefinition = "LONGBLOB")
	private byte[] image;
}
