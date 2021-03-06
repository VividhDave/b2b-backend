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

@Setter
@Getter
@Entity
@Table(name = "logs")
public class Log extends DateAudit implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "message",length = 1000)
	private String message;
	
	@Column(name = "exception",length = 5000)
	private String exception;
	
	@Column(name = "controller_name")
	private String controllerName;
	
	@Column(name = "method_name")
	private String methodName;

}
