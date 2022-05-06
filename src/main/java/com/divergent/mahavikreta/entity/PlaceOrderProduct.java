package com.divergent.mahavikreta.entity;

import com.divergent.mahavikreta.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "place_order_product")
public class PlaceOrderProduct extends DateAudit implements Serializable {

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

    @Column(name = "shipping_charge")
    private double shippingCharge;

    @Column(name = "status")
    private boolean status;

    @Column(name = "order_status")
    private String orderStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "place_order_id", nullable = false)
    @JsonIgnoreProperties(value = { "placeOrderProduct", "hibernateLazyInitializer" })
    private PlaceOrder placeOrder;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Product product;
}
