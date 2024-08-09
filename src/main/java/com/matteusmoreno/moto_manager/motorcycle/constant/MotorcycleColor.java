package com.matteusmoreno.moto_manager.motorcycle.constant;

import lombok.Getter;

@Getter
public enum MotorcycleColor {
    BLACK("Black"),
    WHITE("White"),
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    GRAY("Gray"),
    ORANGE("Orange"),
    SILVER("Silver"),
    PURPLE("Purple"),
    OTHER("Other");

    private final String displayName;

    MotorcycleColor(String displayName) {
        this.displayName = displayName;
    }
}
