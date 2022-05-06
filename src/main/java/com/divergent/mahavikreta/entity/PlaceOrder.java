package com.divergent.mahavikreta.entity;

import com.divergent.mahavikreta.audit.DateAudit;
// import com.divergent.mahavikreta.constants.OrderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import sun.util.calendar.BaseCalendar;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "place_order")
public class PlaceOrder extends DateAudit implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "prod_total_amount")
    private double productTotalAmount;
    
	@Column(name = "shipping_charge",columnDefinition="Decimal(10,2) default '0.00'")
	private double shippingCharge;

	@Column(name = "discount",columnDefinition="Decimal(10,2) default '0.00'")
	private double discount;
	
    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "total_sale_amount")
    private double totalSaleAmount;

    @Column(name = "remark")
    private String remark;
    
    @Column(name = "promo_code")
    private String promoCode;

    @Column(name = "status")
    private String status;
    
    @Column(name="payed_amount",columnDefinition="Decimal(10,2) default '0.00'")
    private double payedAmount;
    
    @Column(name="due_amount",columnDefinition="Decimal(10,2) default '0.00'")
    private double dueAmount;
    
    @Column(name="payed",nullable = false)
    private boolean payed = false;
    
    @Column(name="payment_id")
    private String paymentId;

    // @Column(name ="order_type")
    // private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = {"user", "hibernateLazyInitializer"})
    @JsonInclude(Include.NON_NULL)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    @JsonIgnoreProperties(value = {"deliveryAddress", "hibernateLazyInitializer"})
    @JsonInclude(Include.NON_NULL)
    private UserAddress deliveryAddress;
    
    @OneToMany(mappedBy = "placeOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(ignoreUnknown = true, value = {"placeOrderProduct", "hibernateLazyInitializer"})
    @JsonInclude(Include.NON_NULL)
	private Set<PlaceOrderProduct> placeOrderProduct;

}
