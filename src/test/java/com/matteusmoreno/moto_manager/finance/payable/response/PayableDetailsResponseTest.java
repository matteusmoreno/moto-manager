package com.matteusmoreno.moto_manager.finance.payable.response;

import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Payable Details Response Tests")
class PayableDetailsResponseTest {

    @Test
    @DisplayName("Should return a Payable Details Response successfully")
    void shouldReturnPayableDetailsResponseSuccessfully() {
        Supplier supplier = new Supplier(1L, "SUPPLIER NAME", "00.000.000/0000-00", "(00)000000000", "supplier@email.com", LocalDateTime.now(), null, null, true);
        Payable payable = new Payable(1L, supplier, "Description", BigDecimal.TEN, LocalDate.now(), LocalDate.now(), LocalDate.now(), PaymentStatus.PENDING);

        PayableDetailsResponse payableDetailsResponse = new PayableDetailsResponse(payable);

        assertAll(
            () -> assertEquals(payable.getId(), payableDetailsResponse.id()),
            () -> assertEquals(payable.getSupplier().getName(), payableDetailsResponse.supplierName()),
            () -> assertEquals(payable.getSupplier().getCnpj(), payableDetailsResponse.supplierCnpj()),
            () -> assertEquals(payable.getDescription(), payableDetailsResponse.description()),
            () -> assertEquals(payable.getValue(), payableDetailsResponse.value()),
            () -> assertEquals(payable.getIssueDate(), payableDetailsResponse.issueDate()),
            () -> assertEquals(payable.getDueDate(), payableDetailsResponse.dueDate()),
            () -> assertEquals(payable.getPaymentDate(), payableDetailsResponse.paymentDate()),
            () -> assertEquals(payable.getStatus().getDisplayName(), payableDetailsResponse.status())
        );
    }
}