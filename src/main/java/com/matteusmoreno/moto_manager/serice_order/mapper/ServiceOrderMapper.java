package com.matteusmoreno.moto_manager.serice_order.mapper;

import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
import com.matteusmoreno.moto_manager.exception.MotorcycleNotAssignedException;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.repository.MotorcycleRepository;
import com.matteusmoreno.moto_manager.serice_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.serice_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.serice_order.request.CreateServiceOrderRequest;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.service.ServiceOrderProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ServiceOrderMapper {

    private final MotorcycleRepository motorcycleRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceOrderProductService serviceOrderProductService;

    @Autowired
    public ServiceOrderMapper(MotorcycleRepository motorcycleRepository, EmployeeRepository employeeRepository, ServiceOrderProductService serviceOrderProductService) {
        this.motorcycleRepository = motorcycleRepository;
        this.employeeRepository = employeeRepository;
        this.serviceOrderProductService = serviceOrderProductService;
    }

    public ServiceOrder mapToServiceOrderForCreation(CreateServiceOrderRequest request) {

        Employee seller = employeeRepository.findById(request.sellerId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        if (seller.getRole() != EmployeeRole.SELLER && seller.getRole() != EmployeeRole.MANAGER) throw new EntityNotFoundException("Employee is not a seller or manager");

        Motorcycle motorcycle = motorcycleRepository.findById(request.motorcycleId())
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found"));

        if (motorcycle.getCustomer() == null) throw new MotorcycleNotAssignedException();

        Employee mechanic = employeeRepository.findById(request.mechanicId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (mechanic.getRole() != EmployeeRole.MECHANIC) throw new EntityNotFoundException("Employee is not a mechanic");

        List<ServiceOrderProduct> products = request.products().stream()
                .map(serviceOrderProductService::addProduct)
                .toList();

        BigDecimal totalCost = products.stream()
                .map(ServiceOrderProduct::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).add(request.laborPrice());

        return ServiceOrder.builder()
                .motorcycle(motorcycle)
                .seller(seller)
                .mechanic(mechanic)
                .products(products)
                .description(request.description())
                .laborPrice(request.laborPrice())
                .totalCost(totalCost)
                .serviceOrderStatus(ServiceOrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .startedAt(null)
                .updatedAt(null)
                .completedAt(null)
                .canceledAt(null)
                .build();
    }
}
