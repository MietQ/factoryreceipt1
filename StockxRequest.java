package com.factoryreceipt.factoryreceipt.dto;

public class StockxRequest {

    private String userId;
    private String productName;
    private String price;
    private String styleid;
    private String size;
    private String deliveryTime;
    private String photo;

    // Konstruktor bezargumentowy
    public StockxRequest() {
    }

    // Konstruktor z argumentami (opcjonalny)
    public StockxRequest(String userId, String productName, String price, String styleid, String size, String deliveryTime, String photo) {
        this.userId = userId;
        this.productName = productName;
        this.price = price;
        this.styleid = styleid;
        this.size = size;
        this.deliveryTime = deliveryTime;
        this.photo = photo;
    }

    // Gettery i settery
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStyleid() {
        return styleid;
    }

    public void setStyleid(String styleid) {
        this.styleid = styleid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
