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
@Table(name = "Request_For_Quotation")
public class RequestForQuotation extends DateAudit implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "rfq_code")
    private String rfqCode;

    @Column(name = "total")
    private Double total;

    @Column(name = "max_quantity")
    private int maxQuantity;

    @Column(name = "preferred_unit_price",columnDefinition="Decimal(10,2) default '0.00'")
    private double preferredUnitPrice;

    @Column(name = "details")
    private String details;

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
