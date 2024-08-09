package com.matteusmoreno.moto_manager.product.controller;

import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.product.request.CreateProductRequest;
import com.matteusmoreno.moto_manager.product.request.ProductQuantityUpdateRequest;
import com.matteusmoreno.moto_manager.product.request.UpdateProductRequest;
import com.matteusmoreno.moto_manager.product.response.ProductDetailsResponse;
import com.matteusmoreno.moto_manager.product.service.ProductService;
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

    @PutMapping("/update")
    public ResponseEntity<ProductDetailsResponse> update(@RequestBody @Valid UpdateProductRequest request) {
        Product product = productService.updateProduct(request);

        return ResponseEntity.ok(new ProductDetailsResponse(product));
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        productService.disableProduct(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<ProductDetailsResponse> enable(@PathVariable Long id) {
        Product product = productService.enableProduct(id);

        return ResponseEntity.ok(new ProductDetailsResponse(product));
    }

    @PatchMapping("/add-product")
    public ResponseEntity<ProductDetailsResponse> addProduct(@RequestBody @Valid ProductQuantityUpdateRequest request) {
        Product product = productService.addProduct(request);

        return ResponseEntity.ok(new ProductDetailsResponse(product));
    }

    @PatchMapping("/remove-product")
    public ResponseEntity<ProductDetailsResponse> removeProduct(@RequestBody @Valid ProductQuantityUpdateRequest request) {
        Product product = productService.removeProduct(request);

        return ResponseEntity.ok(new ProductDetailsResponse(product));
    }
}
