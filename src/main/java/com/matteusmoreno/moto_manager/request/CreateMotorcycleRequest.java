package com.matteusmoreno.moto_manager.request;

import com.matteusmoreno.moto_manager.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.constant.MotorcycleColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMotorcycleRequest(
        @NotNull(message = "Motorcycle brand cannot be null")
        MotorcycleBrand motorcycleBrand,
        @NotBlank(message = "Motorcycle model cannot be null")
        String model,
        @NotNull(message = "Motorcycle color cannot be null")
        MotorcycleColor motorcycleColor,
        String plate,
        String year) {
}
