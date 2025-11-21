package com.factoryreceipt.factoryreceipt.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID konta użytkownika, który wygenerował potwierdzenie
    private String userId;

    private String productName;
    private String price;      // sformatowana cena (np. "$299.99")
    private String fee;
    private String shipping;
    private String finalPrice;
    private String orderNumber;
    private String styleid;
    private String size;
    private String deliveryTime;
    private String photo;

    private LocalDateTime createdAt;

    // Konstruktor domyślny
    public Receipt() {}

    // Konstruktor z polami
    public Receipt(String userId, String productName, String price, String fee, String shipping, String finalPrice,
                   String orderNumber, String styleid, String size, String deliveryTime, String photo, LocalDateTime createdAt) {
        this.userId = userId;
        this.productName = productName;
        this.price = price;
        this.fee = fee;
        this.shipping = shipping;
        this.finalPrice = finalPrice;
        this.orderNumber = orderNumber;
        this.styleid = styleid;
        this.size = size;
        this.deliveryTime = deliveryTime;
        this.photo = photo;
        this.createdAt = createdAt;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

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

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
