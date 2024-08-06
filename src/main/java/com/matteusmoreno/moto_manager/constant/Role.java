package com.matteusmoreno.moto_manager.constant;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Admin"),
    SELLER("Seller"),
    MANAGER("Manager");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
