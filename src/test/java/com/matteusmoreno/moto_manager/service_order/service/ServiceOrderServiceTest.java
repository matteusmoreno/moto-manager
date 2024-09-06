package com.matteusmoreno.moto_manager.service_order.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
import com.matteusmoreno.moto_manager.finance.receivable.service.ReceivableService;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.repository.MotorcycleRepository;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.repository.ServiceOrderRepository;
import com.matteusmoreno.moto_manager.service_order.request.CreateServiceOrderRequest;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.service_order.service_order_product.request.ServiceOrderProductRequest;
import com.matteusmoreno.moto_manager.service_order.service_order_product.service.ServiceOrderProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service Order Service Tests")
class ServiceOrderServiceTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private MotorcycleRepository motorcycleRepository;

    @Mock
    private ServiceOrderProductService serviceOrderProductService;

    @Mock
    private ReceivableService receivableService;

    @InjectMocks
    private ServiceOrderService serviceOrderService;

    private Address address;
    private Employee seller;
    private Employee mechanic;
    private Customer customer;
    private Motorcycle motorcycle;
    private Product oleoMotor;
    private Product ledLamp;
    private ServiceOrderProduct serviceOrderProductOleo;
    private ServiceOrderProduct serviceOrderProductLed;
    private ServiceOrderProductRequest serviceOrderProductRequestOleo;
    private ServiceOrderProductRequest serviceOrderProductRequestLed;

    @BeforeEach
    void setup() {
        address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        seller = new Employee(UUID.randomUUID(), "seller", "password", "Seller", "seller@email.com", "(99)999999999", LocalDate.of(2000, 2, 10), Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, address, LocalDateTime.now(), null, null, true);
        mechanic = new Employee(UUID.randomUUID(), "mechanic", "password", "Mechanic", "mechanic@email.com", "(22)222222222", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "888.888.888-88", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);
        customer = new Customer(UUID.randomUUID(), "Customer", "customer@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(11)111111111", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED, "FRP7898", "2010/2011", customer, LocalDateTime.now(), null, null, true);
        oleoMotor = new Product(2L, "OLEO DE MOTOR", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        ledLamp = new Product(3L, "LED LAMP", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        serviceOrderProductOleo = new ServiceOrderProduct(1L, oleoMotor, 1, oleoMotor.getPrice(), oleoMotor.getPrice().multiply(BigDecimal.ONE), null);
        serviceOrderProductLed = new ServiceOrderProduct(2L, ledLamp, 1, ledLamp.getPrice(), ledLamp.getPrice().multiply(BigDecimal.ONE), null);
        serviceOrderProductRequestOleo = new ServiceOrderProductRequest(oleoMotor.getId(), 1);
        serviceOrderProductRequestLed = new ServiceOrderProductRequest(ledLamp.getId(), 1);

    }
    /*
    @Test
    @DisplayName("Should create a Service Order successfully")
    void shouldCreateServiceOrderSuccessfully() {
        List<ServiceOrderProductRequest> products = new ArrayList<>();
        products.add(serviceOrderProductRequestOleo);
        products.add(serviceOrderProductRequestLed);

        CreateServiceOrderRequest request = new CreateServiceOrderRequest(motorcycle.getId(), seller.getId(), mechanic.getId(), products, "troca de óleo e lâmpada de farol", BigDecimal.TEN);

        when(employeeRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(employeeRepository.findById(mechanic.getId())).thenReturn(Optional.of(mechanic));
        when(motorcycleRepository.findById(motorcycle.getId())).thenReturn(Optional.of(motorcycle));
        when(serviceOrderProductService.addProduct(eq(serviceOrderProductRequestOleo), anyLong())).thenReturn(serviceOrderProductOleo);
        when(serviceOrderProductService.addProduct(eq(serviceOrderProductRequestLed), anyLong())).thenReturn(serviceOrderProductLed);

        ServiceOrder result = serviceOrderService.createServiceOrder(request);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(motorcycle, result.getMotorcycle()),
                () -> assertEquals(seller, result.getSeller()),
                () -> assertEquals(mechanic, result.getMechanic()),
                () -> assertEquals("troca de óleo e lâmpada de farol", result.getDescription()),
                () -> assertEquals(BigDecimal.TEN, result.getLaborPrice()),
                () -> assertEquals(BigDecimal.valueOf(20.00), result.getTotalCost()),  // Corrigido para 20.00
                () -> assertEquals(ServiceOrderStatus.PENDING, result.getServiceOrderStatus()),
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNull(result.getStartedAt()),
                () -> assertNull(result.getUpdatedAt()),
                () -> assertNull(result.getCompletedAt()),
                () -> assertNull(result.getCanceledAt())
        );
    }
    */
}