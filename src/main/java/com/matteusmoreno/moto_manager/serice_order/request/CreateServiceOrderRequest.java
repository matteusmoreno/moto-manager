package com.matteusmoreno.moto_manager.serice_order.request;

import com.matteusmoreno.moto_manager.serice_order.service_order_product.request.CreateServiceOrderProductRequest;
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
        List<CreateServiceOrderProductRequest> products,
        String description,
        BigDecimal laborPrice) {
}
