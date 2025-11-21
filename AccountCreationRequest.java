package com.factoryreceipt.factoryreceipt.dto;

public class AccountCreationRequest {
    // Typ konta: "time", "limit", "lifetime"
    private String accountType;
    // Dla kont czasowych: liczba dni (np. 1, 3, 7, 30). Dla lifetime może być pominięta.
    private Integer durationDays;
    // Dla kont limitowanych: liczba użyć (np. 1, 2, 3)
    private Integer usageLimit;

    // Gettery i settery
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public Integer getDurationDays() {
        return durationDays;
    }
    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }
    public Integer getUsageLimit() {
        return usageLimit;
    }
    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }
}
