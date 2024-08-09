package com.matteusmoreno.moto_manager.employee.repository;

import com.matteusmoreno.moto_manager.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Employee findByUsername(String username);

    boolean existsByUsername(String username);
}
