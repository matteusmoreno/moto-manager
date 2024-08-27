package com.matteusmoreno.moto_manager.finance.receivable.repository;

import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivableRepository extends JpaRepository<Receivable, Long> {
    Receivable findByServiceOrder(ServiceOrder serviceOrder);
}
