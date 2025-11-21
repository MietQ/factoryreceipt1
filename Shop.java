package com.factoryreceipt.factoryreceipt.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;       // np. "MediaExpert"
    private String emailFrom;      // e-mail nadawcy
    private String senderName;     // nazwa (imię) nadawcy
    private String templateName;   // nazwa pliku szablonu (bez .html)
    private String defaultSubject; // domyślny tytuł
    @Lob
    private String formConfig;     // JSON z konfiguracją pól

    // Gettery / Settery
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getEmailFrom() {
        return emailFrom;
    }
    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDefaultSubject() {
        return defaultSubject;
    }
    public void setDefaultSubject(String defaultSubject) {
        this.defaultSubject = defaultSubject;
    }

    public String getFormConfig() {
        return formConfig;
    }
    public void setFormConfig(String formConfig) {
        this.formConfig = formConfig;
    }
}
