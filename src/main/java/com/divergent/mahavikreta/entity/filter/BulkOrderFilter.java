package com.divergent.mahavikreta.entity.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BulkOrderFilter {

    private String id;
    private String quantity;
    private String userNegotiatePrice;
    private String adminNegotiatePrice;
    private String orderStatus;
    private String description;
    private String userId;
    private String productId;
    private String userName;
    private String productName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUserNegotiatePrice() {
        return userNegotiatePrice;
    }

    public void setUserNegotiatePrice(String userNegotiatePrice) {
        this.userNegotiatePrice = userNegotiatePrice;
    }

    public String getAdminNegotiatePrice() {
        return adminNegotiatePrice;
    }

    public void setAdminNegotiatePrice(String adminNegotiatePrice) {
        this.adminNegotiatePrice = adminNegotiatePrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
