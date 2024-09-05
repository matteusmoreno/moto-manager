package com.matteusmoreno.moto_manager.supplier.response;

import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Supplier List Response Tests")
class SupplierListResponseTest {

    @Test
    @DisplayName("Should return a Supplier List Response successfully")
    void shouldReturnSupplierListResponseSuccessfully() {
        Supplier supplier = new Supplier(1L, "SUPPLIER NAME", "00.000.000/0000-00", "(00)000000000", "supplier@email.com", LocalDateTime.now(), null, null, true);

        SupplierListResponse result = new SupplierListResponse(supplier);

        assertAll(
            () -> assertEquals(supplier.getId(), result.id()),
            () -> assertEquals(supplier.getName(), result.name()),
            () -> assertEquals(supplier.getCnpj(), result.cnpj()),
            () -> assertEquals(supplier.getPhone(), result.phone()),
            () -> assertEquals(supplier.getEmail(), result.email())
        );
    }
}