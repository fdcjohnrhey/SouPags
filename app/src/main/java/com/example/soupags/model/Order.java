package com.example.soupags.model;

import java.util.ArrayList;

public class Order {
    private String userId;
    private String orderName;
    private String orderPrice;
    private int orderImage;
    private String orderNumber;
    private ArrayList<String> orderList;
    public Order() {
    }

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Order(String orderName, String orderPrice, int orderImage) {
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderImage = orderImage;
        this.orderNumber = orderNumber;
    }

    public ArrayList<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<String> orderList) {
        this.orderList = orderList;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
