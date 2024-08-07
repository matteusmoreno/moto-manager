package com.matteusmoreno.moto_manager.request;

import com.matteusmoreno.moto_manager.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.constant.MotorcycleColor;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateMotorcycleRequest(
        @NotNull(message = "Id is required")
        UUID id,
        MotorcycleBrand brand,
        String model,
        MotorcycleColor color,
        String plate,
        String year) {
}
