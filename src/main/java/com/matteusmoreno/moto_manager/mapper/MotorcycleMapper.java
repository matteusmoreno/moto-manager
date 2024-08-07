package com.matteusmoreno.moto_manager.mapper;

import com.matteusmoreno.moto_manager.entity.Motorcycle;
import com.matteusmoreno.moto_manager.request.CreateMotorcycleRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MotorcycleMapper {

    public Motorcycle mapToMotorcycleForCreation(CreateMotorcycleRequest request)
    {
        return Motorcycle.builder()
                .brand(request.brand())
                .model(request.model().toUpperCase())
                .color(request.color())
                .plate(request.plate().toUpperCase().replaceAll("\\s+", ""))
                .year(request.year())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .active(true)
                .build();
    }
}
