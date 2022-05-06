package com.divergent.mahavikreta.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "prm_header")
public class PromotionHeader extends DateAudit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "buyer_purchases")
    private String buyerPurchases;

    @Column(name = "buyer_purchases_value")
    private int buyerPurchasesValue;

    @Column(name = "product_selection_id")
    private int productSelectionId;

    @Column(name = "buyer_gets")
    private String buyerGets;

    @Column(name = "buyer_gets_value")
    private int buyerGetsValue;

    @Column(name = "applies_to")
    private String appliesTo;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "internal_description")
    private String internalDescription;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "claim_code_type")
    private String claimCodeType;

    @Column(name = "per_customer")
    private boolean perCustomer;

    @Column(name = "claim_code")
    private String claimCode;

    @Column(name = "claim_code_combinability")
    private String claimCodeCombinability;

    @Column(name = "checkout_display_text")
    private String checkoutDisplayText;

    @Column(name = "short_display_text")
    private String shortDisplayText;

    @Column(name = "display_description")
    private String displayDescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prm_type_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    private PromotionType promotionType;

}
