package com.matteusmoreno.moto_manager.repository;

import com.matteusmoreno.moto_manager.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
