package com.matteusmoreno.moto_manager.payable.repository;

import com.matteusmoreno.moto_manager.payable.entity.Payable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayableRepository extends JpaRepository<Payable, Long> {
}
