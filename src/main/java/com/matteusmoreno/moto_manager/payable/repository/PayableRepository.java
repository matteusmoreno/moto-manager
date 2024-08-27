package com.matteusmoreno.moto_manager.payable.repository;

import com.matteusmoreno.moto_manager.payable.PaymentStatus;
import com.matteusmoreno.moto_manager.payable.entity.Payable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PayableRepository extends JpaRepository<Payable, Long> {
    List<Payable> findByStatusAndDueDateBefore(PaymentStatus paymentStatus, LocalDate now);
}
