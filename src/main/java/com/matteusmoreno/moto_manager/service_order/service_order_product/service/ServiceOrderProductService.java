package com.matteusmoreno.moto_manager.service_order.service_order_product.service;

import com.matteusmoreno.moto_manager.exception.InsufficientProductQuantityException;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.product.repository.ProductRepository;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.repository.ServiceOrderRepository;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.service_order.service_order_product.repository.ServiceOrderProductRepository;
import com.matteusmoreno.moto_manager.service_order.service_order_product.request.ServiceOrderProductRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ServiceOrderProductService {

    private final ServiceOrderProductRepository serviceOrderProductRepository;
    private final ProductRepository productRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    @Autowired
    public ServiceOrderProductService(ServiceOrderProductRepository serviceOrderProductRepository, ProductRepository productRepository, ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderProductRepository = serviceOrderProductRepository;
        this.productRepository = productRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional
    public ServiceOrderProduct addProduct(ServiceOrderProductRequest request, Long serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Service Order not found"));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (request.quantity() > product.getQuantity()) throw new InsufficientProductQuantityException(product.getName());

        product.setQuantity(product.getQuantity() - request.quantity());

        ServiceOrderProduct serviceOrderProduct = ServiceOrderProduct.builder()
                .product(product)
                .quantity(request.quantity())
                .unitaryPrice(product.getPrice())
                .finalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.quantity())))
                .serviceOrder(serviceOrder)
                .build();

        serviceOrderProductRepository.save(serviceOrderProduct);

        return serviceOrderProduct;
    }
}
