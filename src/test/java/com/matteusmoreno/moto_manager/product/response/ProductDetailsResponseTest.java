package com.matteusmoreno.moto_manager.product.response;

import com.matteusmoreno.moto_manager.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Product Details Response Tests")
class ProductDetailsResponseTest {

    @Test
    @DisplayName("Should return a Product Details Response successfully")
    void shouldReturnProductDetailsResponseSuccessfully() {
        Product product = new Product(1L, "PRODUCT NAME", "PRODUCT DESCRIPTION", "MANUFACTURER", new BigDecimal("100.00"), 10, LocalDateTime.now(), null, null, true);

        ProductDetailsResponse result = new ProductDetailsResponse(product);

        assertAll(
            () -> assertEquals(product.getId(), result.id()),
            () -> assertEquals(product.getName(), result.name()),
            () -> assertEquals(product.getDescription(), result.description()),
            () -> assertEquals(product.getManufacturer(), result.manufacturer()),
            () -> assertEquals(product.getPrice(), result.price()),
            () -> assertEquals(product.getQuantity(), result.quantity()),
            () -> assertEquals(product.getCreatedAt(), result.createdAt()),
            () -> assertEquals(product.getUpdatedAt(), result.updatedAt()),
            () -> assertEquals(product.getDeletedAt(), result.deletedAt()),
            () -> assertEquals(product.getActive(), result.active())
        );
    }

}