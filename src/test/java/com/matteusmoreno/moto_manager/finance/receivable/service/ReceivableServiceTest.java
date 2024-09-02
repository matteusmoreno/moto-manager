package com.matteusmoreno.moto_manager.finance.receivable.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.exception.PaymentStatusException;
import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.finance.receivable.repository.ReceivableRepository;
import com.matteusmoreno.moto_manager.finance.receivable.response.ReceivableDetailsResponse;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.product.response.ProductDetailsResponse;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Receivable Service Tests")
class ReceivableServiceTest {

    @Mock
    private ReceivableRepository receivableRepository;

    @InjectMocks
    private ReceivableService receivableService;

    private ServiceOrder serviceOrder;
    private Employee seller;
    private Employee mechanic;
    private Customer customer;
    private Motorcycle motorcycle;
    private Product oleoMotor;
    private Product ledLamp;
    private Receivable receivable;

    @BeforeEach
    void setup() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        seller = new Employee(UUID.randomUUID(), "seller", "password", "Seller", "seller@email.com", "(99)999999999", LocalDate.of(2000, 2, 10), Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, address, LocalDateTime.now(), null, null, true);
        mechanic = new Employee(UUID.randomUUID(), "mechanic", "password", "Mechanic", "mechanic@email.com", "(22)222222222", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "888.888.888-88", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);
        customer = new Customer(UUID.randomUUID(), "Customer", "customer@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(11)111111111", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED, "FRP7898", "2010/2011", customer, LocalDateTime.now(), null, null, true);
        oleoMotor = new Product(2L, "OLEO DE MOTOR", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        ledLamp = new Product(3L, "LED LAMP", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        serviceOrder = new ServiceOrder(1L, motorcycle, seller, mechanic, new ArrayList<>(), "troca de pneu e óleo", BigDecimal.TEN, BigDecimal.valueOf(30.00), ServiceOrderStatus.PENDING, LocalDateTime.now(), null, null, null, null);

        ServiceOrderProduct oleoMotorServiceOrderProduct = new ServiceOrderProduct(10L, oleoMotor, 10, BigDecimal.TEN, BigDecimal.TEN.multiply(BigDecimal.TEN), serviceOrder);
        ServiceOrderProduct ledLampServiceOrderProduct = new ServiceOrderProduct(20L, ledLamp, 10, BigDecimal.TEN, BigDecimal.TEN.multiply(BigDecimal.TEN), serviceOrder);

        serviceOrder.getProducts().add(oleoMotorServiceOrderProduct);
        serviceOrder.getProducts().add(ledLampServiceOrderProduct);

        receivable = new Receivable(1L, serviceOrder, serviceOrder.getTotalCost(), serviceOrder.getCreatedAt().toLocalDate(), null, PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("Should create a receivable successfully")
    void shouldCreateReceivableSuccessfully() {

        receivableService.createReceivable(serviceOrder);

        verify(receivableRepository).save(any(Receivable.class));
    }

    @Test
    @DisplayName("Should return a paginated list of receivables")
    void shouldReturnAPaginatedListOfReceivables() {
        Page<Receivable> receivablePage = new PageImpl<>(Collections.singletonList(receivable));
        Pageable pageable = Pageable.ofSize(10);

        when(receivableRepository.findAll(pageable)).thenReturn(receivablePage);

        Page<ReceivableDetailsResponse> responsePage = receivableService.findAllReceivables(pageable);
        ReceivableDetailsResponse response = responsePage.getContent().get(0);

        assertAll(
                () -> assertEquals(receivable.getServiceOrder().getId(), response.serviceOrderId()),
                () -> assertEquals(receivable.getValue(), response.value()),
                () -> assertEquals(receivable.getIssueDate(), response.issueDate()),
                () -> assertEquals(receivable.getPaymentDate(), response.paymentDate()),
                () -> assertEquals(receivable.getStatus().getDisplayName(), response.status())
        );
    }

    @Test
    @DisplayName("Should pay a receivable successfully")
    void shouldPayReceivableSuccessfully() {
        when(receivableRepository.findById(receivable.getId())).thenReturn(Optional.ofNullable(receivable));

        receivableService.payReceivable(receivable.getId());

        verify(receivableRepository).save(receivable);

        assertAll(
                () -> assertEquals(PaymentStatus.PAID, receivable.getStatus()),
                () -> assertNotNull(receivable.getPaymentDate())
        );
    }

    @Test
    @DisplayName("Should return PaymentStatusException when trying to pay a receivable that is not pending")
    void shouldReturnPaymentStatusExceptionWhenTryingToPayAReceivableThatIsNotPending() {
        receivable.setStatus(PaymentStatus.PAID);

        when(receivableRepository.findById(receivable.getId())).thenReturn(Optional.ofNullable(receivable));

        assertThrows(PaymentStatusException.class, () -> receivableService.payReceivable(receivable.getId()));
    }

    @Test
    @DisplayName("Should cancel a receivable successfully")
    void shouldCancelReceivableSuccessfully() {
        when(receivableRepository.findByServiceOrder(serviceOrder)).thenReturn(receivable);

        receivableService.cancelReceivable(serviceOrder);

        verify(receivableRepository).save(receivable);

        assertEquals(PaymentStatus.CANCELED, receivable.getStatus());
    }
}