package com.matteusmoreno.moto_manager.constant;

import lombok.Getter;

@Getter
public enum MotorcycleBrand {
    HONDA("Honda"),
    YAMAHA("Yamaha"),
    SUZUKI("Suzuki"),
    KAWASAKI("Kawasaki"),
    BMW("BMW"),
    HARLEY_DAVIDSON("Harley-Davidson"),
    DUCATI("Ducati"),
    TRIUMPH("Triumph"),
    KTM("KTM"),
    BULL("Bull"),
    SHINERAY("Shineray"),
    OTHER("Other");

    private final String displayName;

    MotorcycleBrand(String displayName) {
        this.displayName = displayName;
    }
}
