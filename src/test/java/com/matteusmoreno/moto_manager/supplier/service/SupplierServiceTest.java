package com.matteusmoreno.moto_manager.supplier.service;

import com.matteusmoreno.moto_manager.exception.SupplierAlreadyExistsException;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.repository.SupplierRepository;
import com.matteusmoreno.moto_manager.supplier.request.CreateSupplierRequest;
import com.matteusmoreno.moto_manager.supplier.request.UpdateSupplierRequest;
import com.matteusmoreno.moto_manager.supplier.response.SupplierDetailsResponse;
import com.matteusmoreno.moto_manager.supplier.response.SupplierListResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Supplier Service Tests")
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private CreateSupplierRequest createSupplierRequest;
    private UpdateSupplierRequest updateSupplierRequest;
    private Supplier supplier;

    @BeforeEach
    void setup() {
         createSupplierRequest = new CreateSupplierRequest("Supplier Name", "00.000.000/0000-00", "(00)000000000", "supplier@email.com");
         updateSupplierRequest = new UpdateSupplierRequest(1L, "New Supplier Name", "11.111.111/1111-11", "(11)111111111", "newsupplieremail@email.com");
         supplier = new Supplier(1L, "SUPPLIER NAME", "00.000.000/0000-00", "(00)000000000", "supplier@email.com", LocalDateTime.now(), null, null, true);
    }

    @Test
    @DisplayName("Should create a new supplier successfully")
    void shouldCreateNewSupplierSuccessfully() {

        Supplier result = supplierService.createSupplier(createSupplierRequest);

        verify(supplierRepository, times(1)).existsByNameAndCnpjIgnoreCase(createSupplierRequest.name(), createSupplierRequest.cnpj());
        verify(supplierRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(createSupplierRequest.name().toUpperCase(), result.getName()),
                () -> assertEquals(createSupplierRequest.cnpj(), result.getCnpj()),
                () -> assertEquals(createSupplierRequest.phone(), result.getPhone()),
                () -> assertEquals(createSupplierRequest.email(), result.getEmail()),
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNull(result.getUpdatedAt()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertTrue(result.getActive())
        );
    }

    @Test
    @DisplayName("Should throw SupplierAlreadyExistsException when trying to create a supplier that already exists")
    void shouldThrowSupplierAlreadyExistsException() {
        when(supplierRepository.existsByNameAndCnpjIgnoreCase(createSupplierRequest.name(), createSupplierRequest.cnpj())).thenReturn(true);

        assertThrows(SupplierAlreadyExistsException.class, () -> supplierService.createSupplier(createSupplierRequest));
    }

    @Test
    @DisplayName("Should return a paginated list of suppliers")
    void shouldReturnPaginatedListOfSuppliers() {
        Page<Supplier> supplierPage = new PageImpl<>(Collections.singletonList(supplier));
        Pageable pageable = Pageable.ofSize(10);

        when(supplierRepository.findAll(pageable)).thenReturn(supplierPage);

        Page<SupplierListResponse> result = supplierService.listAllSuppliers(pageable);
        SupplierListResponse response = result.getContent().get(0);

        verify(supplierRepository, times(1)).findAll(pageable);

        assertAll(
                () -> assertEquals(supplier.getId(), response.id()),
                () -> assertEquals(supplier.getName(), response.name()),
                () -> assertEquals(supplier.getCnpj(), response.cnpj()),
                () -> assertEquals(supplier.getPhone(), response.phone()),
                () -> assertEquals(supplier.getEmail(), response.email())
        );
    }

    @Test
    @DisplayName("Should update a supplier successfully")
    void shouldUpdateSupplierSuccessfully() {

        when(supplierRepository.findById(updateSupplierRequest.id())).thenReturn(Optional.ofNullable(supplier));

        Supplier result = supplierService.updateSupplier(updateSupplierRequest);

        verify(supplierRepository, times(1)).findById(updateSupplierRequest.id());
        verify(supplierRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(updateSupplierRequest.name().toUpperCase(), result.getName()),
                () -> assertEquals(updateSupplierRequest.cnpj(), result.getCnpj()),
                () -> assertEquals(updateSupplierRequest.phone(), result.getPhone()),
                () -> assertEquals(updateSupplierRequest.email(), result.getEmail()),
                () -> assertNotNull(result.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when trying to update a supplier that does not exist")
    void shouldThrowEntityNotFoundExceptionWhenUpdatingSupplier() {
        when(supplierRepository.findById(updateSupplierRequest.id())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> supplierService.updateSupplier(updateSupplierRequest));
    }

    @Test
    @DisplayName("Should disable a supplier successfully")
    void shouldDisableSupplierSuccessfully() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.ofNullable(supplier));

        supplierService.disableSupplier(1L);

        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, times(1)).save(supplier);

        assertAll(
                () -> assertFalse(supplier.getActive()),
                () -> assertNotNull(supplier.getDeletedAt())
        );
    }

    @Test
    @DisplayName("Should enable a supplier successfully")
    void shouldEnableSupplierSuccessfully() {
        supplier.setActive(false);
        supplier.setDeletedAt(LocalDateTime.now());

        when(supplierRepository.findById(1L)).thenReturn(Optional.ofNullable(supplier));

        Supplier result = supplierService.enableSupplier(1L);

        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, times(1)).save(supplier);

        assertAll(
                () -> assertTrue(result.getActive()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertNotNull(result.getUpdatedAt())
        );
    }

}