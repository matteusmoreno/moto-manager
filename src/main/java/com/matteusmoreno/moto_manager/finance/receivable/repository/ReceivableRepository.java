package com.matteusmoreno.moto_manager.finance.receivable.repository;

import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivableRepository extends JpaRepository<Receivable, Long> {
}
