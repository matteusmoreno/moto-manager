package com.matteusmoreno.moto_manager.product.mapper;

import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.product.request.CreateProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;
    
    @Test
    @DisplayName("Should map CreateProductRequest to Product correctly")
    void shouldMapCreateProductRequestToProductCorrectly() {

        CreateProductRequest createRequest = new CreateProductRequest("Product name", "Description",
                "Manufacturer", BigDecimal.TEN, 10);

        Product result = productMapper.mapToProductForCreation(createRequest);

        assertNull(result.getId());
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
}