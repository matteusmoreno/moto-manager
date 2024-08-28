package com.matteusmoreno.moto_manager.finance.receivable.repository;

import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReceivableRepository extends JpaRepository<Receivable, Long> {

    Receivable findByServiceOrder(ServiceOrder serviceOrder);

    @Query("SELECT r FROM Receivable r WHERE r.issueDate BETWEEN :startDate AND :endDate AND r.status <> 'CANCELED'")
    List<Receivable> findReceivablesWithinDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
