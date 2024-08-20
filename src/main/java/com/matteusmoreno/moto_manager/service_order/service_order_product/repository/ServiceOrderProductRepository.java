package com.matteusmoreno.moto_manager.service_order.service_order_product.repository;

import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderProductRepository extends JpaRepository<ServiceOrderProduct, Long> {
}
