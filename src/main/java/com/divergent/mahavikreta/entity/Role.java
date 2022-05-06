package com.divergent.mahavikreta.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;

import com.divergent.mahavikreta.audit.DateAudit;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "role")
public class Role extends DateAudit implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;
	public static final String NAME_FIELD = "name";
	public static final String DISPLAY_NAME = "display_Name";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NaturalId
	@Column(name = NAME_FIELD, nullable = false)
	private String name;

	@NaturalId
	@Column(name = DISPLAY_NAME, nullable = false)
	private String displayName;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "name"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "name"))
	private Collection<Privileges> privileges;
	

	@Override
	public String getAuthority() {
		return this.name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Collection<Privileges> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Collection<Privileges> privileges) {
		this.privileges = privileges;
	}
}
