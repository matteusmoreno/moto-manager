package com.matteusmoreno.moto_manager.service_order.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Service Order Service Tests")
@ExtendWith(MockitoExtension.class)
class ServiceOrderServiceTest {
/*
    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private MotorcycleRepository motorcycleRepository;

    @Mock
    private ServiceOrderProductService serviceOrderProductService;

    @InjectMocks
    private ServiceOrderService serviceOrderService;
    private Employee seller;
    private Employee mechanic;
    private Customer customer;
    private Motorcycle motorcycle;
    private Product oleoMotor;
    private Product ledLamp;
    private ServiceOrder serviceOrder;
    private CreateServiceOrderRequest request;

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
        request = new CreateServiceOrderRequest(motorcycle.getId(), seller.getId(), mechanic.getId(), new ArrayList<>(), "description", BigDecimal.TEN);
    }

    @Test
    @DisplayName("Should create a new Service Order successfully")
    void shouldCreateANewServiceOrderSuccessfully() {
        customer.getMotorcycles().add(motorcycle);

        ServiceOrderProduct oleoMotorServiceOrderProduct = new ServiceOrderProduct(10L, oleoMotor, 10, BigDecimal.TEN, BigDecimal.TEN.multiply(BigDecimal.TEN), serviceOrder);
        ServiceOrderProduct ledLampServiceOrderProduct = new ServiceOrderProduct(20L, ledLamp, 10, BigDecimal.TEN, BigDecimal.TEN.multiply(BigDecimal.TEN), serviceOrder);

        ServiceOrderProductRequest oleoMotorRequest = new ServiceOrderProductRequest(oleoMotor.getId(), 10);
        ServiceOrderProductRequest ledLampRequest = new ServiceOrderProductRequest(ledLamp.getId(), 10);

        request.products().add(oleoMotorRequest);
        request.products().add(ledLampRequest);

        when(employeeRepository.findById(request.sellerId())).thenReturn(Optional.of(seller));
        when(motorcycleRepository.findById(request.motorcycleId())).thenReturn(Optional.of(motorcycle));
        when(employeeRepository.findById(request.mechanicId())).thenReturn(Optional.of(mechanic));
        when(serviceOrderProductService.addProduct(oleoMotorRequest, anyLong())).thenReturn(oleoMotorServiceOrderProduct);
        when(serviceOrderProductService.addProduct(ledLampRequest, anyLong())).thenReturn(ledLampServiceOrderProduct);


        ServiceOrder result = serviceOrderService.createServiceOrder(request);

        verify(serviceOrderRepository, times(2)).save(result);

        assertAll(
                () -> assertEquals(ServiceOrderStatus.PENDING, result.getServiceOrderStatus()),
                () -> assertEquals(BigDecimal.valueOf(40.00), result.getTotalCost()),
                () -> assertEquals(BigDecimal.valueOf(30.00), result.getLaborPrice()),
                () -> assertEquals(BigDecimal.valueOf(10.00), result.getProducts().get(0).getUnitaryPrice()),
                () -> assertEquals(BigDecimal.valueOf(100.00), result.getProducts().get(0).getFinalPrice())
        );

    }

 */
}