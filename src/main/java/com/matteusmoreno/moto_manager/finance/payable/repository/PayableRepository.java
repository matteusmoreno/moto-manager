package com.matteusmoreno.moto_manager.finance.payable.repository;

import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PayableRepository extends JpaRepository<Payable, Long> {
    List<Payable> findByStatusAndDueDateBefore(PaymentStatus paymentStatus, LocalDate now);
}
