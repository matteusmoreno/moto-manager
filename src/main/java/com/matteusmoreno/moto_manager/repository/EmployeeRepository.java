package com.matteusmoreno.moto_manager.repository;

import com.matteusmoreno.moto_manager.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Employee findByUsername(String username);

    boolean existsByUsername(String username);
}
