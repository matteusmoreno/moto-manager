package com.matteusmoreno.moto_manager.service_order.request;

import com.matteusmoreno.moto_manager.service_order.service_order_product.request.ServiceOrderProductRequest;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record UpdateServiceOrderRequest(
        @NotNull(message = "Service Order id is required")
        Long id,
        UUID motorcycleId,
        UUID sellerId,
        UUID mechanicId,
        List<ServiceOrderProductRequest> products,
        String description,
        BigDecimal laborPrice) {
}
