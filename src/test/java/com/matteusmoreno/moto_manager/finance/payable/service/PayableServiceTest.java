package com.matteusmoreno.moto_manager.finance.payable.service;

import com.matteusmoreno.moto_manager.exception.InvalidDueDateException;
import com.matteusmoreno.moto_manager.exception.PayableAlreadyCanceledException;
import com.matteusmoreno.moto_manager.exception.PayableAlreadyPaidException;
import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.finance.payable.repository.PayableRepository;
import com.matteusmoreno.moto_manager.finance.payable.request.CreatePayableRequest;
import com.matteusmoreno.moto_manager.finance.payable.request.UpdatePayableRequest;
import com.matteusmoreno.moto_manager.finance.payable.response.PayableDetailsResponse;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.repository.SupplierRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payable Service Tests")
class PayableServiceTest {

    @Mock
    private PayableRepository payableRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private PayableService payableService;

    private Supplier supplier;
    private Payable payable;

    @BeforeEach
    void setup() {
        supplier = new Supplier(10L, "Supplier", "99.999.999/0001-99","(99)999999999", "supplier@email.com", LocalDateTime.now(), null, null, true);
        payable = new Payable(1L, supplier, "Description", BigDecimal.valueOf(100.00), LocalDate.now(), LocalDate.now().plusDays(10), null, null);
    }

    @Test
    @DisplayName("Should create a payable successfully with pending status")
    void shouldCreatePayableSuccessfullyWithPendingStatus() {
        CreatePayableRequest request = new CreatePayableRequest(supplier.getId(), "Description", BigDecimal.valueOf(100.00), LocalDate.now(), LocalDate.now().plusDays(10));

        when(supplierRepository.findById(request.supplierId())).thenReturn(Optional.ofNullable(supplier));

        Payable result = payableService.createPayable(request);

        verify(supplierRepository, times(1)).findById(request.supplierId());
        verify(payableRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(supplier, result.getSupplier()),
                () -> assertEquals(request.description().toUpperCase(), result.getDescription()),
                () -> assertEquals(request.value(), result.getValue()),
                () -> assertEquals(request.issueDate(), result.getIssueDate()),
                () -> assertEquals(request.dueDate(), result.getDueDate()),
                () -> assertNull(result.getPaymentDate()),
                () -> assertEquals("PENDING", result.getStatus().name())
        );
    }

        @Test
        @DisplayName("Should return InvalidDueDateException when due date is greater than issue date")
        void shouldReturnInvalidDueDateExceptionWhenDueDateIsGreaterThanIssueDate() {
            CreatePayableRequest request = new CreatePayableRequest(supplier.getId(), "Description", BigDecimal.valueOf(100.00), LocalDate.now(), LocalDate.now().minusDays(10));

            when(supplierRepository.findById(request.supplierId())).thenReturn(Optional.ofNullable(supplier));

            assertThrows(InvalidDueDateException.class, () -> payableService.createPayable(request));

            verify(supplierRepository, times(1)).findById(request.supplierId());
        }

    @Test
    @DisplayName("Should create a payable successfully with overdue status")
    void shouldCreatePayableSuccessfullyWithOverdueStatus() {
        CreatePayableRequest request = new CreatePayableRequest(supplier.getId(), "Description", BigDecimal.valueOf(100.00), LocalDate.of(1990, 8, 28), LocalDate.of(1990,8,28).plusDays(10));

        when(supplierRepository.findById(request.supplierId())).thenReturn(Optional.ofNullable(supplier));

        Payable result = payableService.createPayable(request);

        verify(supplierRepository, times(1)).findById(request.supplierId());
        verify(payableRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(supplier, result.getSupplier()),
                () -> assertEquals(request.description().toUpperCase(), result.getDescription()),
                () -> assertEquals(request.value(), result.getValue()),
                () -> assertEquals(request.issueDate(), result.getIssueDate()),
                () -> assertEquals(request.dueDate(), result.getDueDate()),
                () -> assertNull(result.getPaymentDate()),
                () -> assertEquals("OVERDUE", result.getStatus().name())
        );
    }

    @Test
    @DisplayName("Should return a paginated list of payables")
    void shouldReturnAPaginatedListOfPayables() {
        payable.setStatus(PaymentStatus.PENDING);

        Page<Payable> payablePage = new PageImpl<>(Collections.singletonList(payable));
        Pageable pageable = Pageable.ofSize(10);

        when(payableRepository.findAll(pageable)).thenReturn(payablePage);

        Page<PayableDetailsResponse> result = payableService.findAllPayables(pageable);
        PayableDetailsResponse response = result.getContent().get(0);

        verify(payableRepository, times(1)).findAll(pageable);

        assertAll(
                () -> assertEquals(payable.getId(), response.id()),
                () -> assertEquals(payable.getSupplier().getName(), response.supplierName()),
                () -> assertEquals(payable.getDescription(), response.description()),
                () -> assertEquals(payable.getValue(), response.value()),
                () -> assertEquals(payable.getIssueDate(), response.issueDate()),
                () -> assertEquals(payable.getDueDate(), response.dueDate()),
                () -> assertEquals(payable.getPaymentDate(), response.paymentDate()),
                () -> assertEquals(payable.getStatus().name(), response.status())
        );
    }

    @Test
    @DisplayName("Should update a payable successfully")
    void shouldUpdateAPayableSuccessfully() {
        Supplier newSupplier = new Supplier(2L, "New Supplier", "99.999.999/0001-99","(99)999999999", "newsupplier@email.com", LocalDateTime.now(), null, null, true);

        UpdatePayableRequest request = new UpdatePayableRequest(1L, 2L, "New Description", BigDecimal.valueOf(200.00), LocalDate.now(), LocalDate.now().plusDays(20));

        when(payableRepository.findById(payable.getId())).thenReturn(Optional.of(payable));
        when(supplierRepository.findById(request.supplierId())).thenReturn(Optional.of(newSupplier));

        Payable result = payableService.updatePayable(request);

        verify(supplierRepository, times(1)).findById(request.supplierId());
        verify(payableRepository, times(1)).findById(1L);
        verify(payableRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(newSupplier, result.getSupplier()),
                () -> assertEquals(payable.getId(), result.getId()),
                () -> assertEquals(payable.getSupplier(), result.getSupplier()),
                () -> assertEquals("New Description", result.getDescription()),
                () -> assertEquals(BigDecimal.valueOf(200.00), result.getValue()),
                () -> assertEquals(LocalDate.now(), result.getIssueDate()),
                () -> assertEquals(LocalDate.now().plusDays(20), result.getDueDate()),
                () -> assertNull(result.getPaymentDate())
        );
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when trying to update a non-existent payable")
    void shouldReturnEntityNotFoundExceptionWhenTryingToUpdateANonExistentPayable() {
        UpdatePayableRequest request = new UpdatePayableRequest(2L, 2L, "New Description", BigDecimal.valueOf(200.00), LocalDate.now(), LocalDate.now().plusDays(20));

        when(payableRepository.findById(request.payableId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> payableService.updatePayable(request));

    }

    @Test
    @DisplayName("Should return EntityNotFoundException when trying to update a non-existent supplier")
    void shouldReturnEntityNotFoundExceptionWhenTryingToUpdateANonExistentSupplier() {
        UpdatePayableRequest request = new UpdatePayableRequest(1L, 2L, "New Description", BigDecimal.valueOf(200.00), LocalDate.now(), LocalDate.now().plusDays(20));

        when(payableRepository.findById(request.payableId())).thenReturn(Optional.of(payable));
        when(supplierRepository.findById(request.supplierId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> payableService.updatePayable(request));
    }

    @Test
    @DisplayName("Should pay a payable successfully")
    void shouldPayAPayableSuccessfully() {
        payable.setStatus(PaymentStatus.PENDING);

        when(payableRepository.findById(payable.getId())).thenReturn(Optional.of(payable));

        Payable result = payableService.payPayable(payable.getId());

        verify(payableRepository, times(1)).findById(payable.getId());
        verify(payableRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(payable.getId(), result.getId()),
                () -> assertEquals(payable.getSupplier(), result.getSupplier()),
                () -> assertEquals(payable.getDescription(), result.getDescription()),
                () -> assertEquals(payable.getValue(), result.getValue()),
                () -> assertEquals(payable.getIssueDate(), result.getIssueDate()),
                () -> assertEquals(payable.getDueDate(), result.getDueDate()),
                () -> assertNotNull(result.getPaymentDate()),
                () -> assertEquals("PAID", result.getStatus().name())
        );
    }

    @Test
    @DisplayName("Should return PayableAlreadyPaidException when trying to pay an already paid payable")
    void shouldReturnPayableAlreadyPaidExceptionWhenTryingToPayAnAlreadyPaidPayable() {
        payable.setStatus(PaymentStatus.PAID);

        when(payableRepository.findById(payable.getId())).thenReturn(Optional.of(payable));

        assertThrows(PayableAlreadyPaidException.class, () -> payableService.payPayable(payable.getId()));

        verify(payableRepository, times(1)).findById(payable.getId());
    }

    @Test
    @DisplayName("Should return PayableAlreadyCanceledException when trying to pay a canceled payable")
    void shouldReturnPayableAlreadyCanceledExceptionWhenTryingToPayACanceledPayable() {
        payable.setStatus(PaymentStatus.CANCELED);

        when(payableRepository.findById(payable.getId())).thenReturn(Optional.of(payable));

        assertThrows(PayableAlreadyCanceledException.class, () -> payableService.payPayable(payable.getId()));

        verify(payableRepository, times(1)).findById(payable.getId());
    }

    @Test
    @DisplayName("Should cancel a payable successfully")
    void shouldCancelAPayableSuccessfully() {
        payable.setStatus(PaymentStatus.PENDING);

        when(payableRepository.findById(payable.getId())).thenReturn(Optional.of(payable));

        Payable result = payableService.cancelPayable(payable.getId());

        verify(payableRepository, times(1)).findById(payable.getId());
        verify(payableRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(payable.getId(), result.getId()),
                () -> assertEquals(payable.getSupplier(), result.getSupplier()),
                () -> assertEquals(payable.getDescription(), result.getDescription()),
                () -> assertEquals(payable.getValue(), result.getValue()),
                () -> assertEquals(payable.getIssueDate(), result.getIssueDate()),
                () -> assertEquals(payable.getDueDate(), result.getDueDate()),
                () -> assertNull(result.getPaymentDate()),
                () -> assertEquals("CANCELED", result.getStatus().name())
        );
    }

    @Test
    @DisplayName("Should return PayableAlreadyPaidException when trying to cancel an already paid payable")
    void shouldReturnPayableAlreadyPaidExceptionWhenTryingToCancelAnAlreadyPaidPayable() {
        payable.setStatus(PaymentStatus.PAID);

        when(payableRepository.findById(payable.getId())).thenReturn(Optional.of(payable));

        assertThrows(PayableAlreadyPaidException.class, () -> payableService.cancelPayable(payable.getId()));

        verify(payableRepository, times(1)).findById(payable.getId());
    }

    @Test
    @DisplayName("Should return PayableAlreadyCanceledException when trying to cancel a canceled payable")
    void shouldReturnPayableAlreadyCanceledExceptionWhenTryingToCancelACanceledPayable() {
        payable.setStatus(PaymentStatus.CANCELED);

        when(payableRepository.findById(payable.getId())).thenReturn(Optional.of(payable));

        assertThrows(PayableAlreadyCanceledException.class, () -> payableService.cancelPayable(payable.getId()));

        verify(payableRepository, times(1)).findById(payable.getId());
    }

    @Test
    @DisplayName("Should update payables status to OVERDUE every day at 00:00")
    void shouldUpdatePayablesStatusToOverdueEveryDayAt00_00() {
        Payable overduePayable = new Payable(2L, supplier, "Overdue Payable", BigDecimal.valueOf(100.00), LocalDate.now().minusDays(10), LocalDate.now().minusDays(5), null, null);
        when(payableRepository.findByStatusAndDueDateBefore(PaymentStatus.PENDING, LocalDate.now())).thenReturn(Collections.singletonList(overduePayable));

        payableService.updateOverduePayables();

        verify(payableRepository, times(1)).findByStatusAndDueDateBefore(PaymentStatus.PENDING, LocalDate.now());
        verify(payableRepository, times(1)).save(overduePayable);

        assertEquals(PaymentStatus.OVERDUE, overduePayable.getStatus());
    }
}
