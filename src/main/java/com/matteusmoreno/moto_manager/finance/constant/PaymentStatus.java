package com.matteusmoreno.moto_manager.finance.constant;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("Pending"),
    PAID("Paid"),
    OVERDUE("Overdue"),
    CANCELED("Canceled");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = this.name();
    }
}
