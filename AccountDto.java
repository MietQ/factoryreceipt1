package com.factoryreceipt.factoryreceipt.dto;

import java.time.LocalDateTime;

public class AccountDto {
    private String userId;
    private String email;           // Dodane pole email
    private String accountType;
    private Integer usageLimit;
    private LocalDateTime expirationDate;

    // Konstruktor
    public AccountDto(String userId, String email, String accountType, Integer usageLimit, LocalDateTime expirationDate) {
        this.userId = userId;
        this.email = email;
        this.accountType = accountType;
        this.usageLimit = usageLimit;
        this.expirationDate = expirationDate;
    }

    // Gettery i settery
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
