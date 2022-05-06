package com.divergent.mahavikreta.mapper;

import com.divergent.mahavikreta.entity.PlaceOrder;

public class PlaceOrderMapper {

    public static PlaceOrder getPlaceOrderDetails(PlaceOrder placeOrder) {
        placeOrder.getUser().setUserAddresses(null);
        placeOrder.getDeliveryAddress().setUser(null);
//        placeOrder.getPlaceOrderProduct().setPlaceOrder(null);
//        placeOrder.getPlaceOrderProduct().getProduct().setBrand(null);
//        placeOrder.getPlaceOrderProduct().getProduct().setCategory(null);
//        placeOrder.getPlaceOrderProduct().getProduct().setSubCategory(null);
//        placeOrder.getPlaceOrderProduct().getProduct().setChilledCategory(null);
//        placeOrder.getPlaceOrderProduct().getProduct().setProductAttribute(null);
//        placeOrder.getPlaceOrderProduct().getProduct().setProductImage(null);
        return placeOrder;
    }
}
