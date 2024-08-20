package com.matteusmoreno.moto_manager.service_order.request;

import com.matteusmoreno.moto_manager.service_order.service_order_product.request.ServiceOrderProductRequest;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateServiceOrderRequest(
        @NotNull(message = "Motorcycle id is required")
        UUID motorcycleId,
        @NotNull(message = "Seller id or Manager id is required")
        UUID sellerId,
        @NotNull(message = "Mechanic id is required")
        UUID mechanicId,
        List<ServiceOrderProductRequest> products,
        String description,
        BigDecimal laborPrice) {
}
