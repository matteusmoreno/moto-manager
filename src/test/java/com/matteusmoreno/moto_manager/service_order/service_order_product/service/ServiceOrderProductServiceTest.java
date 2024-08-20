package com.matteusmoreno.moto_manager.service_order.service_order_product.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.exception.InsufficientProductQuantityException;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.product.repository.ProductRepository;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.repository.ServiceOrderRepository;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.service_order.service_order_product.repository.ServiceOrderProductRepository;
import com.matteusmoreno.moto_manager.service_order.service_order_product.request.ServiceOrderProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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

@DisplayName("Service Order Product Service Tests")
@ExtendWith(MockitoExtension.class)
class ServiceOrderProductServiceTest {

    @Mock
    private ServiceOrderProductRepository serviceOrderProductRepository;

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ServiceOrderProductService serviceOrderProductService;

    private Customer customer;
    private Motorcycle motorcycle;
    private Product oleoMotor;
    private ServiceOrder serviceOrder;
    private ServiceOrderProductRequest request;


    @BeforeEach
    void setup() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        Employee seller = new Employee(UUID.randomUUID(), "seller", "password", "Seller", "seller@email.com", "(99)999999999", LocalDate.of(2000, 2, 10),
                Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, address, LocalDateTime.now(), null, null, true);
        Employee mechanic = new Employee(UUID.randomUUID(), "mechanic", "password", "Mechanic", "mechanic@email.com", "(22)222222222", LocalDate.of(1990, 8, 28),
                Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "888.888.888-88", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);
        customer = new Customer(UUID.randomUUID(), "Customer", "customer@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(),
                "(11)111111111", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED,
                "FRP7898", "2010/2011", customer, LocalDateTime.now(), null, null, true);
        oleoMotor = new Product(2L, "OLEO DE MOTOR", "Description", "MANUFACTURER", BigDecimal.TEN, 10,
                LocalDateTime.now(), null, null, true);
        serviceOrder = new ServiceOrder(1L, motorcycle, seller, mechanic, new ArrayList<>(), "troca de pneu e óleo",
                BigDecimal.TEN, BigDecimal.valueOf(30.00), ServiceOrderStatus.PENDING, LocalDateTime.now(), null, null, null, null);
        request = new ServiceOrderProductRequest(oleoMotor.getId(), 5);
    }

    @Test
    @DisplayName("Should add Service Order Product correctly")
    void shouldAddServiceOrderProductCorrectly() {
        customer.getMotorcycles().add(motorcycle);

        when(serviceOrderRepository.findById(serviceOrder.getId())).thenReturn(Optional.of(serviceOrder));
        when(productRepository.findById(oleoMotor.getId())).thenReturn(Optional.of(oleoMotor));

        ServiceOrderProduct result = serviceOrderProductService.addProduct(request, serviceOrder.getId());

        verify(serviceOrderRepository, times(1)).findById(serviceOrder.getId());
        verify(productRepository, times(1)).findById(request.productId());
        verify(serviceOrderProductRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(oleoMotor, result.getProduct()),
                () -> assertEquals(request.quantity(), result.getQuantity()),
                () -> assertEquals(oleoMotor.getPrice(), result.getUnitaryPrice()),
                () -> assertEquals(result.getUnitaryPrice().multiply(BigDecimal.valueOf(request.quantity())) , result.getFinalPrice()),
                () -> assertEquals(serviceOrder.getId(), result.getServiceOrder().getId())
        );
    }

    @Test
    @DisplayName("Should return InsufficientProductQuantityException when more quantity is added to the service order than is available in stock")
    void shouldReturnInsufficientProductQuantityExceptionWhenMoreQuantityIsAddedToTheServiceOrderThanIsAvailableInStock() {
        customer.getMotorcycles().add(motorcycle);

        ServiceOrderProductRequest newRequest = new ServiceOrderProductRequest(oleoMotor.getId(), 11);

        when(serviceOrderRepository.findById(serviceOrder.getId())).thenReturn(Optional.of(serviceOrder));
        when(productRepository.findById(oleoMotor.getId())).thenReturn(Optional.of(oleoMotor));

        Executable executable = () -> serviceOrderProductService.addProduct(newRequest, serviceOrder.getId());
        assertThrows(InsufficientProductQuantityException.class, executable);

        verify(serviceOrderRepository, times(1)).findById(serviceOrder.getId());
        verify(productRepository, times(1)).findById(request.productId());
        verify(serviceOrderProductRepository, times(0)).save(any());
    }
}