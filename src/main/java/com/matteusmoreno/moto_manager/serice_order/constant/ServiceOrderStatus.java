package com.matteusmoreno.moto_manager.serice_order.constant;

import lombok.Getter;

@Getter
public enum ServiceOrderStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELED("Canceled");

    private final String displayName;

    ServiceOrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
