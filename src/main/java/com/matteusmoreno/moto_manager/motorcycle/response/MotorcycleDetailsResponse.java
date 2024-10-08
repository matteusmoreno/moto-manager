package com.matteusmoreno.moto_manager.motorcycle.response;

import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;

import java.time.LocalDateTime;
import java.util.UUID;

public record MotorcycleDetailsResponse(
        UUID id,
        MotorcycleBrand brand,
        String model,
        MotorcycleColor color,
        String plate,
        String year,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public MotorcycleDetailsResponse(Motorcycle motorcycle) {
        this(
                motorcycle.getId(),
                motorcycle.getBrand(),
                motorcycle.getModel(),
                motorcycle.getColor(),
                motorcycle.getPlate(),
                motorcycle.getYear(),
                motorcycle.getCreatedAt(),
                motorcycle.getUpdatedAt(),
                motorcycle.getDeletedAt(),
                motorcycle.getActive());
    }
}
