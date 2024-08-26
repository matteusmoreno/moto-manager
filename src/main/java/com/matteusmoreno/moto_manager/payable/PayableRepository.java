package com.matteusmoreno.moto_manager.payable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PayableRepository extends JpaRepository<Payable, Long> {
}
