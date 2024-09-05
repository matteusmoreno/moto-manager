package com.matteusmoreno.moto_manager.supplier.response;

import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("Supplier Details Response Tests")
class SupplierDetailsResponseTest {

    @Test
    @DisplayName("Should return a Supplier Details Response successfully")
    void shouldReturnSupplierDetailsResponseSuccessfully() {
        Supplier supplier = new Supplier(1L, "SUPPLIER NAME", "00.000.000/0000-00", "(00)000000000", "supplier@email.com", LocalDateTime.now(), null, null, true);

        SupplierDetailsResponse result = new SupplierDetailsResponse(supplier);

        Assertions.assertAll(
            () -> Assertions.assertEquals(supplier.getId(), result.id()),
            () -> Assertions.assertEquals(supplier.getName(), result.name()),
            () -> Assertions.assertEquals(supplier.getCnpj(), result.cnpj()),
            () -> Assertions.assertEquals(supplier.getPhone(), result.phone()),
            () -> Assertions.assertEquals(supplier.getEmail(), result.email()),
            () -> Assertions.assertEquals(supplier.getCreatedAt(), result.createdAt()),
            () -> Assertions.assertEquals(supplier.getUpdatedAt(), result.updatedAt()),
            () -> Assertions.assertEquals(supplier.getDeletedAt(), result.deletedAt()),
            () -> Assertions.assertEquals(supplier.getActive(), result.active())
        );
    }
}