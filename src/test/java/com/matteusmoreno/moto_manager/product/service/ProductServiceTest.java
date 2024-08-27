package com.matteusmoreno.moto_manager.product.service;

import com.matteusmoreno.moto_manager.exception.InsufficientProductQuantityException;
import com.matteusmoreno.moto_manager.exception.ProductAlreadyExistsException;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.product.mapper.ProductMapper;
import com.matteusmoreno.moto_manager.product.repository.ProductRepository;
import com.matteusmoreno.moto_manager.product.request.CreateProductRequest;
import com.matteusmoreno.moto_manager.product.request.ProductQuantityUpdateRequest;
import com.matteusmoreno.moto_manager.product.request.UpdateProductRequest;
import com.matteusmoreno.moto_manager.product.response.ProductDetailsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Product Service Tests")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private CreateProductRequest createRequest;
    private UpdateProductRequest updateRequest;
    private ProductQuantityUpdateRequest productQuantityUpdateRequest;
    private Product product;

    @BeforeEach
    void setup() {
        createRequest = new CreateProductRequest("Product name", "Description",
                "Manufacturer", BigDecimal.TEN, 10);

        updateRequest = new UpdateProductRequest(1L, "Updated name", "Updated manufacturer",
                "Updated description", BigDecimal.ONE);

        productQuantityUpdateRequest = new ProductQuantityUpdateRequest(1L, 10);

        product = new Product(1L, "PRODUCT NAME", "Description", "MANUFACTURER", BigDecimal.TEN, 10,
                LocalDateTime.now(), null, null, true);
    }

    @Test
    @DisplayName("Should create a new product successfully")
    void shouldCreateANewProductSuccessfully() {

        when(productRepository.existsByNameAndManufacturerIgnoreCase(createRequest.name(), createRequest.manufacturer())).thenReturn(false);
        when(productMapper.mapToProductForCreation(createRequest)).thenReturn(product);

        Product result = productService.createProduct(createRequest);

        verify(productRepository, times(1)).existsByNameAndManufacturerIgnoreCase(createRequest.name(), createRequest.manufacturer());
        verify(productMapper, times(1)).mapToProductForCreation(createRequest);
        verify(productRepository, times(1)).save(result);

        assertEquals(createRequest.name().toUpperCase(), result.getName());
        assertEquals(createRequest.description(), result.getDescription());
        assertEquals(createRequest.manufacturer().toUpperCase(), result.getManufacturer());
        assertEquals(createRequest.price(), result.getPrice());
        assertEquals(createRequest.quantity(), result.getQuantity());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertNull(result.getDeletedAt());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("Should throws EntityNotFoundException if the name and manufacturer match")
    void shouldThrowsEntityNotFoundExceptionIfNameAndManufacturerMatch() {

        when(productRepository.existsByNameAndManufacturerIgnoreCase(createRequest.name(), createRequest.manufacturer())).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(createRequest));
    }

    @Test
    @DisplayName("Should return a paginated list of products")
    void shouldReturnAPaginatedListOfProducts() {

        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        Pageable pageable = Pageable.ofSize(10);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDetailsResponse> responsePage = productService.findAllProducts(pageable);
        ProductDetailsResponse response = responsePage.getContent().get(0);

        assertEquals(1, responsePage.getTotalElements());
        assertEquals(product.getId(), response.id());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getDescription(), response.description());
        assertEquals(product.getManufacturer(), response.manufacturer());
        assertEquals(product.getPrice(), response.price());
        assertEquals(product.getQuantity(), response.quantity());
        assertEquals(product.getCreatedAt(), response.createdAt());
        assertEquals(product.getUpdatedAt(), response.updatedAt());
        assertEquals(product.getDeletedAt(), response.deletedAt());
        assertEquals(product.getActive(), response.active());
    }

    @Test
    @DisplayName("Should update a product successfully")
    void shouldUpdateAProductSuccessfully() {

        when(productRepository.findById(updateRequest.id())).thenReturn(Optional.ofNullable(product));

        Product result = productService.updateProduct(updateRequest);

        verify(productRepository, times(1)).findById(updateRequest.id());
        verify(productRepository, times(1)).save(result);

        assertEquals(product.getId(), result.getId());
        assertEquals(updateRequest.name().toUpperCase(), result.getName());
        assertEquals(updateRequest.description(), result.getDescription());
        assertEquals(updateRequest.manufacturer().toUpperCase(), result.getManufacturer());
        assertEquals(updateRequest.price(), result.getPrice());
        assertEquals(product.getQuantity(), result.getQuantity());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertNull(result.getDeletedAt());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if the product ID does not exist in the database")
    void shouldThrowEntityNotFoundExceptionIfProductIdDoesNotExist() {

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(updateRequest));

        verify(productRepository, times(1)).findById(any());
        verify(productRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Should disable a product successfully")
    void shouldDisableProductSuccessfully() {
        Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(product));

        productService.disableProduct(id);

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(product);

        assertFalse(product.getActive());
        assertNotNull(product.getDeletedAt());
    }

    @Test
    @DisplayName("Should enable a product successfully")
    void shouldEnableProductSuccessfully() {
        Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(product));

        Product result = productService.enableProduct(id);

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(result);

        assertTrue(result.getActive());
        assertNull(result.getDeletedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should add more quantity to the product successfully")
    void shouldAddMoreQuantityToProductSuccessfully() {

        when(productRepository.findById(productQuantityUpdateRequest.id())).thenReturn(Optional.ofNullable(product));

        Product result = productService.incrementProductQuantity(productQuantityUpdateRequest);

        verify(productRepository, times(1)).findById(productQuantityUpdateRequest.id());
        verify(productRepository, times(1)).save(result);

        assertEquals(20, result.getQuantity());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should reduce the product quantity successfully")
    void shouldReduceProductQuantitySuccessfully() {

        when(productRepository.findById(productQuantityUpdateRequest.id())).thenReturn(Optional.ofNullable(product));

        Product result = productService.reduceProductQuantity(productQuantityUpdateRequest);

        verify(productRepository, times(1)).findById(productQuantityUpdateRequest.id());
        verify(productRepository, times(1)).save(result);

        assertEquals(0, result.getQuantity());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw InsufficientProductQuantityException when removing more than available quantity")
    void shouldThrowInsufficientProductQuantityExceptionWhenRemovingMoreThanAvailableQuantity() {
        ProductQuantityUpdateRequest request = new ProductQuantityUpdateRequest(1L, 200);

        when(productRepository.findById(request.id())).thenReturn(Optional.ofNullable(product));

        assertThrows(InsufficientProductQuantityException.class, () -> productService.reduceProductQuantity(request));
    }
}