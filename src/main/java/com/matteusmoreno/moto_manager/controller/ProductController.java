package com.matteusmoreno.moto_manager.controller;

import com.matteusmoreno.moto_manager.entity.Product;
import com.matteusmoreno.moto_manager.request.CreateProductRequest;
import com.matteusmoreno.moto_manager.response.ProductDetailsResponse;
import com.matteusmoreno.moto_manager.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDetailsResponse> create(@RequestBody @Valid CreateProductRequest request, UriComponentsBuilder uriBuilder) {
        Product product = productService.createProduct(request);
        URI uri = uriBuilder.path("/products/create/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(uri).body(new ProductDetailsResponse(product));
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<ProductDetailsResponse>> findAll(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<ProductDetailsResponse> page = productService.findAllProducts(pageable);

        return ResponseEntity.ok(page);
    }
}
