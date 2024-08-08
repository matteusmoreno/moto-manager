package com.matteusmoreno.moto_manager.service;

import com.matteusmoreno.moto_manager.entity.Product;
import com.matteusmoreno.moto_manager.mapper.ProductMapper;
import com.matteusmoreno.moto_manager.repository.ProductRepository;
import com.matteusmoreno.moto_manager.request.CreateProductRequest;
import com.matteusmoreno.moto_manager.request.UpdateProductRequest;
import com.matteusmoreno.moto_manager.response.ProductDetailsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productBuilder) {
        this.productRepository = productRepository;
        this.productMapper = productBuilder;
    }

    @Transactional
    public Product createProduct(CreateProductRequest request) {

        if (productRepository.existsByNameAndManufacturerIgnoreCase(request.name(), request.manufacturer())) {
            Product product = productRepository.findByNameAndManufacturerIgnoreCase(request.name(), request.manufacturer());
            product.setQuantity(product.getQuantity() + request.quantity());
            product.setPrice(request.price());
            product.setDescription(request.description());
            product.setUpdatedAt(LocalDateTime.now());
            return product;
        }

        Product product = productMapper.mapToProductForCreation(request);
        productRepository.save(product);

        return product;
    }

    public Page<ProductDetailsResponse> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDetailsResponse::new);
    }

    @Transactional
    public Product updateProduct(UpdateProductRequest request) {
        Product product = productRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (request.name() != null) product.setName(request.name().toUpperCase());
        if (request.description() != null) product.setDescription(request.description());
        if (request.manufacturer() != null) product.setManufacturer(request.manufacturer().toUpperCase());
        if (request.price() != null) product.setPrice(request.price());

        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        return product;
    }
}
