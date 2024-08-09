package com.matteusmoreno.moto_manager.serice_order.service_order_product.response;

import com.matteusmoreno.moto_manager.serice_order.service_order_product.entity.ServiceOrderProduct;

import java.math.BigDecimal;

public record ServiceOrderProductResponse(
        String productName,
        String productManufacturer,
        Integer quantity,
        BigDecimal unitaryPrice,
        BigDecimal finalPrice) {

    public ServiceOrderProductResponse(ServiceOrderProduct serviceOrderProduct) {
        this(
                serviceOrderProduct.getProduct().getName(),
                serviceOrderProduct.getProduct().getManufacturer(),
                serviceOrderProduct.getQuantity(),
                serviceOrderProduct.getUnitaryPrice(),
                serviceOrderProduct.getFinalPrice()
        );
    }
}
