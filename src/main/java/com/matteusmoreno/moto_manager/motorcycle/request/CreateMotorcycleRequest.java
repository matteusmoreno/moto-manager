package com.matteusmoreno.moto_manager.motorcycle.request;

import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMotorcycleRequest(
        @NotNull(message = "Motorcycle brand cannot be null")
        MotorcycleBrand brand,
        @NotBlank(message = "Motorcycle model cannot be null")
        String model,
        @NotNull(message = "Motorcycle color cannot be null")
        MotorcycleColor color,
        String plate,
        String year) {
}
