package com.matteusmoreno.moto_manager.repository;

import com.matteusmoreno.moto_manager.entity.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID> {
}
