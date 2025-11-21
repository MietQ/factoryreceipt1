package com.factoryreceipt.factoryreceipt.dto;

import java.time.LocalDateTime;

public class AccountCreationResponse {
    private String userId;
    private String password;
    private String accountType;
    private LocalDateTime expirationDate;
    private Integer usageLimit;

    public AccountCreationResponse(String userId, String password, String accountType, LocalDateTime expirationDate, Integer usageLimit) {
        this.userId = userId;
        this.password = password;
        this.accountType = accountType;
        this.expirationDate = expirationDate;
        this.usageLimit = usageLimit;
    }

    // Gettery i settery
    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public String getAccountType() {
        return accountType;
    }
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
    public Integer getUsageLimit() {
        return usageLimit;
    }
}
