package com.matteusmoreno.moto_manager.finance.payable.repository;

import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PayableRepository extends JpaRepository<Payable, Long> {

    List<Payable> findByStatusAndDueDateBefore(PaymentStatus paymentStatus, LocalDate now);

    @Query("SELECT p FROM Payable p WHERE p.issueDate BETWEEN :startDate AND :endDate AND p.status <> 'CANCELED'")
    List<Payable> findPayablesWithinDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
