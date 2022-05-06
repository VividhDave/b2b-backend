package com.divergent.mahavikreta.entity;

import com.divergent.mahavikreta.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "bulk_order")
public class BulkOrder extends DateAudit implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "user_negotiate_price",columnDefinition="Decimal(10,2) default '0.00'")
    private double userNegotiatePrice;

    @Column(name = "admin_negotiate_price",columnDefinition="Decimal(10,2) default '0.00'")
    private double adminNegotiatePrice;

    @Column(name = "description")
    private String description;

    @Column(name = "comment")
    private String comment;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "status")
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(ignoreUnknown = true, value = {"user", "hibernateLazyInitializer"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties(ignoreUnknown = true, value = {"product", "hibernateLazyInitializer"})
    private Product product;

}
