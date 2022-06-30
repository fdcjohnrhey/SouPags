package com.example.soupags.model;

public class Cart {
    private String userId;
    private String orderName;
    private String orderPrice;
    private int orderImage;
    private String cartId;
    public Cart(){

    }

    public Cart(String orderName, String orderPrice, int orderImage, String cartId) {
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderImage = orderImage;
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(int orderImage) {
        this.orderImage = orderImage;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }
}
