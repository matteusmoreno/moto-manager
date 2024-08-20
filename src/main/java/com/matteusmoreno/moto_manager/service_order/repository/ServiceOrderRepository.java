package com.matteusmoreno.moto_manager.service_order.repository;

import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
}
