package com.matteusmoreno.moto_manager.service;

import com.matteusmoreno.moto_manager.entity.Product;
import com.matteusmoreno.moto_manager.mapper.ProductMapper;
import com.matteusmoreno.moto_manager.repository.ProductRepository;
import com.matteusmoreno.moto_manager.request.CreateProductRequest;
import com.matteusmoreno.moto_manager.response.ProductDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            return product;
        }

        Product product = productMapper.mapToProductForCreation(request);
        productRepository.save(product);

        return product;
    }

    public Page<ProductDetailsResponse> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDetailsResponse::new);
    }
}
