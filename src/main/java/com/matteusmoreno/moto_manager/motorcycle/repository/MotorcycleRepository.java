package com.matteusmoreno.moto_manager.motorcycle.repository;

import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID> {

    boolean existsByPlate(String plate);
}
