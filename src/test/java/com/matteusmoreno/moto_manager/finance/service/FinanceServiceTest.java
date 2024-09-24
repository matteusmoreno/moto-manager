package com.matteusmoreno.moto_manager.finance.service;

import com.lowagie.text.DocumentException;
import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.exception.PdfReportGenerationException;
import com.matteusmoreno.moto_manager.finance.PdfReportGenerator;
import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.entity.Finance;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.finance.payable.repository.PayableRepository;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.finance.receivable.repository.ReceivableRepository;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Finance Service Tests")
class FinanceServiceTest {

    @Mock
    private ReceivableRepository receivableRepository;

    @Mock
    private PayableRepository payableRepository;

    @Mock
    private PdfReportGenerator pdfReportGenerator;

    @InjectMocks
    private FinanceService financeService;

    private Receivable receivable;
    private Payable payable;

    @BeforeEach
    void setUp() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        Employee seller = new Employee(UUID.randomUUID(), "seller", "password", "Seller", "seller@email.com", "(99)999999999", LocalDate.of(2000, 2, 10), Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, address, LocalDateTime.now(), null, null, true);
        Employee mechanic = new Employee(UUID.randomUUID(), "mechanic", "password", "Mechanic", "mechanic@email.com", "(22)222222222", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "888.888.888-88", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);
        Customer customer = new Customer(UUID.randomUUID(), "Customer", "customer@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(11)111111111", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        Product oleoMotor = new Product(2L, "OLEO DE MOTOR", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        Product ledLamp = new Product(3L, "LED LAMP", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        Motorcycle motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED, "FRP7898", "2010/2011", customer, LocalDateTime.now(), null, null, true);

        ServiceOrder serviceOrder = new ServiceOrder(1L, motorcycle, seller, mechanic, new ArrayList<>(), "troca de pneu e óleo", BigDecimal.TEN, BigDecimal.valueOf(30.00), ServiceOrderStatus.PENDING, LocalDateTime.now(), null, null, null, null);
        ServiceOrderProduct oleoMotorServiceOrderProduct = new ServiceOrderProduct(10L, oleoMotor, 10, BigDecimal.TEN, BigDecimal.TEN.multiply(BigDecimal.TEN), serviceOrder);
        ServiceOrderProduct ledLampServiceOrderProduct = new ServiceOrderProduct(20L, ledLamp, 10, BigDecimal.TEN, BigDecimal.TEN.multiply(BigDecimal.TEN), serviceOrder);
        serviceOrder.getProducts().add(oleoMotorServiceOrderProduct);
        serviceOrder.getProducts().add(ledLampServiceOrderProduct);

        receivable = new Receivable(1L, serviceOrder, serviceOrder.getTotalCost(), serviceOrder.getCreatedAt().toLocalDate(), null, PaymentStatus.PENDING);

        Supplier supplier = new Supplier(10L, "Supplier", "99.999.999/0001-99","(99)999999999", "supplier@email.com", LocalDateTime.now(), null, null, true);
        payable = new Payable(1L, supplier, "Description", BigDecimal.valueOf(100.00), LocalDate.of(2021,10,10), LocalDate.of(2021,11,10), null, PaymentStatus.PENDING);

    }

    @Test
    @DisplayName("Should generate a weekly report successfully")
    void shouldGenerateWeeklyReportSuccessfully() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        when(receivableRepository.findReceivablesWithinDateRange(startDate, endDate)).thenReturn(List.of(receivable));
        when(payableRepository.findPayablesWithinDateRange(startDate, endDate)).thenReturn(List.of(payable));

        financeService.generateWeeklyReport();

        verify(receivableRepository, times(1)).findReceivablesWithinDateRange(startDate, endDate);
        verify(payableRepository, times(1)).findPayablesWithinDateRange(startDate, endDate);
    }

    @Test
    @DisplayName("Should generate a monthly report successfully")
    void shouldGenerateMonthlyReportSuccessfully() {
        int year = 2021;
        int month = 10;
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());

        when(receivableRepository.findReceivablesWithinDateRange(startDate, endDate)).thenReturn(List.of(receivable));
        when(payableRepository.findPayablesWithinDateRange(startDate, endDate)).thenReturn(List.of(payable));

        financeService.generateMonthlyReport(year, month);

        verify(receivableRepository, times(1)).findReceivablesWithinDateRange(startDate, endDate);
        verify(payableRepository, times(1)).findPayablesWithinDateRange(startDate, endDate);
    }

    @Test
    @DisplayName("Should generate a yearly report successfully")
    void shouldGenerateYearlyReportSuccessfully() {
        int year = 2021;
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        when(receivableRepository.findReceivablesWithinDateRange(startDate, endDate)).thenReturn(List.of(receivable));
        when(payableRepository.findPayablesWithinDateRange(startDate, endDate)).thenReturn(List.of(payable));

        financeService.generateYearlyReport(year);

        verify(receivableRepository, times(1)).findReceivablesWithinDateRange(startDate, endDate);
        verify(payableRepository, times(1)).findPayablesWithinDateRange(startDate, endDate);
    }

    @Test
    @DisplayName("Should throw IOException when generating PDF report")
    void shouldThrowIOExceptionWhenGeneratingPdfReport() throws DocumentException, IOException {
        Finance finance = new Finance(10L, "Report Name", LocalDate.now(), List.of(receivable), List.of(payable), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO);

        doThrow(IOException.class).when(pdfReportGenerator).generateReportPdf(any(), any());

        assertThrows(PdfReportGenerationException.class, () -> financeService.generateAndSavePdfReport(finance, "report-name-"));
    }

    @Test
    @DisplayName("Should throw DocumentException when generating PDF report")
    void shouldThrowDocumentExceptionWhenGeneratingPdfReport() throws DocumentException, IOException {
        Finance finance = new Finance(10L, "Report Name", LocalDate.now(), List.of(receivable), List.of(payable), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO);

        doThrow(DocumentException.class).when(pdfReportGenerator).generateReportPdf(any(), any());

        assertThrows(PdfReportGenerationException.class, () -> financeService.generateAndSavePdfReport(finance, "report-name-"));
    }
}