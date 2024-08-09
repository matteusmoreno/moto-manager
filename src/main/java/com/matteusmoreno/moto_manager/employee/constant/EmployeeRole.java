package com.matteusmoreno.moto_manager.employee.constant;

import lombok.Getter;

@Getter
public enum EmployeeRole {
    ADMIN("Admin"),
    SELLER("Seller"),
    MECHANIC("Mechanic"),
    MANAGER("Manager");

    private final String displayName;

    EmployeeRole(String displayName) {
        this.displayName = displayName;
    }
}
