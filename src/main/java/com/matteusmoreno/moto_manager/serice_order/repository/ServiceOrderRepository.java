package com.matteusmoreno.moto_manager.serice_order.repository;

import com.matteusmoreno.moto_manager.serice_order.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
}
