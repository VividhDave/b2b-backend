package com.divergent.mahavikreta.entity.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentFilter {

    private String id;
    private String created_by;
    private String created_date;
    private String last_modified_date;
    private String last_modified_by;
    private String account_id;
    private String amount;
    private String amount_refunded;
    private String amount_transferred;
    private String bank;
    private String base_amount;
    private String captured;
    private String card_id;
    private String contact;
    private String currency;
    private String description;
    private String email;
    private String  entity;
    private String error_code;
    private String error_description;
    private String error_reason;
    private String error_source;
    private String error_step;
    private String event;
    private String fee;
    private String international;
    private String rzpay_invoice_id;
    private String method;
    private String rzpay_order_id;
    private String refund_payment_id;
    private String refund_status;
    private String status;
    private String tax;
    private String transaction_id;
    private String user_id;
    private String vpa;
    private String wallet;
    private String customer_id;
    private String user_name;
    private String virtual_account_id;
}

