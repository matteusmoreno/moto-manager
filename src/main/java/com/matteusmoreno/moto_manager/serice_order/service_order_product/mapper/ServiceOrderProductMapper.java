package com.matteusmoreno.moto_manager.serice_order.service_order_product.mapper;

import com.matteusmoreno.moto_manager.exception.InsufficientProductQuantityException;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.product.repository.ProductRepository;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.request.CreateServiceOrderProductRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ServiceOrderProductMapper {

    private final ProductRepository productRepository;

    @Autowired
    public ServiceOrderProductMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ServiceOrderProduct mapToServiceOrderProductForCreation(CreateServiceOrderProductRequest request) {

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (request.quantity() > product.getQuantity()) throw new InsufficientProductQuantityException();

        product.setQuantity(product.getQuantity() - request.quantity());

        return ServiceOrderProduct.builder()
                .product(product)
                .quantity(request.quantity())
                .unitaryPrice(product.getPrice())
                .finalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.quantity())))
                .build();
    }
}
