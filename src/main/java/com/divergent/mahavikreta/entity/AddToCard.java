package com.divergent.mahavikreta.entity;

import com.divergent.mahavikreta.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "add_to_card")
public class AddToCard extends DateAudit implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "qty")
    private int qty;

    @Column(name = "price",columnDefinition="Decimal(10,2) default '0.00'")
    private double price;

    @Column(name = "delivery_cost",columnDefinition="Decimal(10,2) default '0.00'")
    private double deliveryCost;

    @Column(name = "status")
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(ignoreUnknown = true, value = { "roles", "hibernateLazyInitializer" })
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties(ignoreUnknown = true, value = { "brand", "hibernateLazyInitializer" })
    private Product product;
}
