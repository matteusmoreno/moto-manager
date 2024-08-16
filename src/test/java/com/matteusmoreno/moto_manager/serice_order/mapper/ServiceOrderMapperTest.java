package com.matteusmoreno.moto_manager.serice_order.mapper;

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
import com.matteusmoreno.moto_manager.serice_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.serice_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.serice_order.request.CreateServiceOrderRequest;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.request.CreateServiceOrderProductRequest;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.service.ServiceOrderProductService;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service Order Mapper Tests")
class ServiceOrderMapperTest {

    @Mock
    private MotorcycleRepository motorcycleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ServiceOrderProductService serviceOrderProductService;

    @InjectMocks
    private ServiceOrderMapper serviceOrderMapper;

    private UUID customerId;
    private Customer customer;
    private Employee seller;
    private Employee mechanic;
    private Motorcycle motorcycle;
    private Product product1;
    private Product product2;
    private CreateServiceOrderProductRequest createServiceOrderProductRequest1;
    private CreateServiceOrderProductRequest createServiceOrderProductRequest2;
    private List<CreateServiceOrderProductRequest> products;
    private CreateServiceOrderRequest request;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        customer = new Customer(
                customerId,
                "Name",
                "email@email.com",
                LocalDate.of(1990, 8, 28),
                Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(),
                "(99)999999999",
                new ArrayList<>(),
                new ArrayList<>(),
                LocalDateTime.now(),
                null,
                null,
                true
        );

        seller = new Employee(
                UUID.randomUUID(),
                "matteus",
                "senha",
                "Matteus Moreno",
                "matteus@email.com",
                "(99)999999999",
                LocalDate.of(1990, 8, 28),
                Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(),
                "222.222.222-22",
                EmployeeRole.SELLER,
                null,
                LocalDateTime.now(),
                null,
                null,
                true
        );

        mechanic = new Employee(
                UUID.randomUUID(),
                "fernando",
                "senha",
                "Fernando",
                "fernando@email.com",
                "(66)666666666",
                LocalDate.of(2000, 2, 10),
                Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(),
                "666.666.666-66",
                EmployeeRole.MECHANIC,
                null,
                LocalDateTime.now(),
                null,
                null,
                true
        );

        motorcycle = new Motorcycle(
                UUID.randomUUID(),
                MotorcycleBrand.HONDA,
                "Biz 100",
                MotorcycleColor.RED,
                "FRP7898",
                "2010/2011",
                customer,
                LocalDateTime.now(),
                null,
                null,
                true
        );

        product1 = new Product(
                1L,
                "RELAÇÃO BIZ 100",
                "coroa + corrente + pinhão",
                "VAZ",
                BigDecimal.valueOf(129.90),
                10,
                LocalDateTime.now(),
                null,
                null,
                true
        );

        product2 = new Product(
                2L,
                "PNEU DIANTEIRO BIZ",
                "pneu misto",
                "VIPAL",
                BigDecimal.valueOf(189.00),
                5,
                LocalDateTime.now(),
                null,
                null,
                true
        );

        createServiceOrderProductRequest1 = new CreateServiceOrderProductRequest(product1.getId(), 1);
        createServiceOrderProductRequest2 = new CreateServiceOrderProductRequest(product2.getId(), 2);
        products = List.of(createServiceOrderProductRequest1, createServiceOrderProductRequest2);

        request = new CreateServiceOrderRequest(
                motorcycle.getId(),
                seller.getId(),
                mechanic.getId(),
                products,
                "troca de relação e pneu",
                BigDecimal.valueOf(80.00)
        );
    }

    @Test
    @DisplayName("Should map CreateServiceOrderRequest to ServiceOrder correctly")
        void shouldMapCreateServiceOrderRequestToServiceOrderCorrectly() {

        when(employeeRepository.findById(request.sellerId())).thenReturn(Optional.of(seller));
        when(employeeRepository.findById(request.mechanicId())).thenReturn(Optional.of(mechanic));
        when(motorcycleRepository.findById(request.motorcycleId())).thenReturn(Optional.of(motorcycle));

        ServiceOrderProduct serviceOrderProduct1 = new ServiceOrderProduct();
        serviceOrderProduct1.setFinalPrice(product1.getPrice());
        ServiceOrderProduct serviceOrderProduct2 = new ServiceOrderProduct();
        serviceOrderProduct2.setFinalPrice(product2.getPrice());

        when(serviceOrderProductService.addProduct(createServiceOrderProductRequest1)).thenReturn(serviceOrderProduct1);
        when(serviceOrderProductService.addProduct(createServiceOrderProductRequest2)).thenReturn(serviceOrderProduct2);

        ServiceOrder serviceOrder = serviceOrderMapper.mapToServiceOrderForCreation(request);

        assertNotNull(serviceOrder);
        assertEquals(motorcycle, serviceOrder.getMotorcycle());
        assertEquals(seller, serviceOrder.getSeller());
        assertEquals(mechanic, serviceOrder.getMechanic());
        assertEquals("troca de relação e pneu", serviceOrder.getDescription());
        assertEquals(BigDecimal.valueOf(80.00), serviceOrder.getLaborPrice());
        assertEquals(
                product1.getPrice().add(product2.getPrice()).add(request.laborPrice()),
                serviceOrder.getTotalCost()
        );
        assertEquals(ServiceOrderStatus.PENDING, serviceOrder.getServiceOrderStatus());
        assertNotNull(serviceOrder.getCreatedAt());
        assertNull(serviceOrder.getStartedAt());
        assertNull(serviceOrder.getUpdatedAt());
        assertNull(serviceOrder.getCompletedAt());
        assertNull(serviceOrder.getCanceledAt());
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when the employee responsible for the Service Order does not have the role of Seller or Manager")
    void shouldReturnEntityNotFoundExceptionWhenTheEmployeeResponsibleForTheServiceOrderDoesNotHaveTheRoleOfSellerOrManager() {
        when(employeeRepository.findById(request.sellerId())).thenReturn(Optional.of(mechanic));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> serviceOrderMapper.mapToServiceOrderForCreation(request));

        assertEquals("Employee is not a seller or manager", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when the employee responsible for the Service Order is not found")
    void shouldThrowEntityNotFoundExceptionWhenTheEmployeeResponsibleForTheServiceOrderIsNotFound() {
        when(employeeRepository.findById(request.sellerId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> serviceOrderMapper.mapToServiceOrderForCreation(request));
        assertEquals("Seller not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when the motorcycle is not found")
    void shouldThrowEntityNotFoundExceptionWhenTheMotorcycleIsNotFound() {

        when(employeeRepository.findById(request.sellerId())).thenReturn(Optional.ofNullable(seller));
        when(motorcycleRepository.findById(request.motorcycleId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> serviceOrderMapper.mapToServiceOrderForCreation(request));
        assertEquals("Motorcycle not found", exception.getMessage());
    }
}
