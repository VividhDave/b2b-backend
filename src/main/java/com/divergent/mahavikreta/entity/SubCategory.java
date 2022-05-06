package com.divergent.mahavikreta.entity;

import java.io.Serializable;
import java.util.HashSet;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import com.divergent.mahavikreta.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subcategory")
public class SubCategory extends DateAudit implements Serializable {

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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	@JsonIgnoreProperties(value = {"subcategory", "hibernateLazyInitializer"})
	private Category category;
	
	@OneToMany(mappedBy = "subCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value = {"subCategory", "hibernateLazyInitializer"})
	private Set<ChilledCategory> chilledCategory = new HashSet<>();

	@Column(name = "image", columnDefinition = "LONGBLOB")
	private byte[] image;

	@Column(name = "gst_percentage")
	private Double gst;

}
