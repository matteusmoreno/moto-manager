package com.matteusmoreno.moto_manager.service_order.response;

import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.service_order_product.response.ServiceOrderProductResponse;

import java.math.BigDecimal;
import java.util.List;

public record ServiceOrderResponse(
        Long id,
        String customerName,
        MotorcycleBrand motorcycleBrand,
        String motorcycleModel,
        String motorcycleYear,
        MotorcycleColor motorcycleColor,
        String seller,
        String mechanic,
        List<ServiceOrderProductResponse> products,
        String description,
        BigDecimal laborPrice,
        BigDecimal totalCost,
        ServiceOrderStatus serviceOrderStatus) {

    public ServiceOrderResponse(ServiceOrder serviceOrder) {
        this(
                serviceOrder.getId(),
                serviceOrder.getMotorcycle().getCustomer().getName(),
                serviceOrder.getMotorcycle().getBrand(),
                serviceOrder.getMotorcycle().getModel(),
                serviceOrder.getMotorcycle().getYear(),
                serviceOrder.getMotorcycle().getColor(),
                serviceOrder.getSeller().getName(),
                serviceOrder.getMechanic().getName(),
                serviceOrder.getProducts().stream().map(ServiceOrderProductResponse::new).toList(),
                serviceOrder.getDescription(),
                serviceOrder.getLaborPrice(),
                serviceOrder.getTotalCost(),
                serviceOrder.getServiceOrderStatus()
                );
    }
}
