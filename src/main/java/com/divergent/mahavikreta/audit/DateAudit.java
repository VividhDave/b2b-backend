package com.divergent.mahavikreta.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DateAudit {

	public static final String CREATED_BY = "created_by";
	public static final String CREATED_DATE = "created_date";
	public static final String LAST_MODIFIED_BY = "last_modified_by";
	public static final String LAST_MODIFIED_DATE = "last_modified_date";

	@CreatedBy
	@Column(name = CREATED_BY)
	private String createdBy;

	@CreatedDate
	@Column(name = CREATED_DATE)
	private Date createdDate;

	@LastModifiedBy
	@Column(name = LAST_MODIFIED_BY)
	private String updatedBy;

	@LastModifiedDate
	@Column(name = LAST_MODIFIED_DATE)
	private Date lastModifiedDate;

}
