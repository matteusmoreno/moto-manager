package com.matteusmoreno.moto_manager.service_order.service;

import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
import com.matteusmoreno.moto_manager.exception.MotorcycleNotAssignedException;
import com.matteusmoreno.moto_manager.exception.ServiceOrderStatusException;
import com.matteusmoreno.moto_manager.finance.receivable.service.ReceivableService;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.repository.MotorcycleRepository;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.repository.ServiceOrderRepository;
import com.matteusmoreno.moto_manager.service_order.request.CreateServiceOrderRequest;
import com.matteusmoreno.moto_manager.service_order.request.UpdateServiceOrderRequest;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.service_order.service_order_product.service.ServiceOrderProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final EmployeeRepository employeeRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final ServiceOrderProductService serviceOrderProductService;
    private final ReceivableService receivableService;

    @Autowired
    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository, EmployeeRepository employeeRepository, MotorcycleRepository motorcycleRepository, ServiceOrderProductService serviceOrderProductService, ReceivableService receivableService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.employeeRepository = employeeRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.serviceOrderProductService = serviceOrderProductService;
        this.receivableService = receivableService;
    }

    @Transactional
    public ServiceOrder createServiceOrder(CreateServiceOrderRequest request) {
        Employee seller = employeeRepository.findById(request.sellerId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        if (seller.getRole() != EmployeeRole.SELLER && seller.getRole() != EmployeeRole.MANAGER) throw new EntityNotFoundException("Employee is not a seller or manager");

        Motorcycle motorcycle = motorcycleRepository.findById(request.motorcycleId())
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found"));

        if (motorcycle.getCustomer() == null) throw new MotorcycleNotAssignedException();

        Employee mechanic = employeeRepository.findById(request.mechanicId())
                .orElseThrow(() -> new EntityNotFoundException("Mechanic not found"));

        if (mechanic.getRole() != EmployeeRole.MECHANIC) throw new EntityNotFoundException("Employee is not a mechanic");

        ServiceOrder serviceOrder = ServiceOrder.builder()
                .motorcycle(motorcycle)
                .seller(seller)
                .mechanic(mechanic)
                .products(new ArrayList<>())
                .description(request.description())
                .laborPrice(request.laborPrice())
                .totalCost(null)
                .serviceOrderStatus(ServiceOrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .startedAt(null)
                .updatedAt(null)
                .completedAt(null)
                .canceledAt(null)
                .build();

        serviceOrderRepository.save(serviceOrder);

        List<ServiceOrderProduct> products = request.products().stream()
                .map(product -> serviceOrderProductService.addProduct(product, serviceOrder.getId()))
                .toList();

        BigDecimal totalCost = products.stream()
                .map(ServiceOrderProduct::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).add(request.laborPrice());

        serviceOrder.getProducts().addAll(products);
        serviceOrder.setTotalCost(totalCost);

        serviceOrderRepository.save(serviceOrder);

        return serviceOrder;
    }

    @Transactional
    public ServiceOrder startServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service order not found"));

        if (serviceOrder.getServiceOrderStatus() != ServiceOrderStatus.PENDING) throw new ServiceOrderStatusException("Service order is not pending");

        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.IN_PROGRESS);
        serviceOrder.setStartedAt(LocalDateTime.now());

        serviceOrderRepository.save(serviceOrder);

        return serviceOrder;
    }

    @Transactional
    public ServiceOrder completeServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service order not found"));

        if (serviceOrder.getServiceOrderStatus() != ServiceOrderStatus.IN_PROGRESS) throw new ServiceOrderStatusException("Service order is not in progress");

        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.COMPLETED);
        serviceOrder.setCompletedAt(LocalDateTime.now());

        serviceOrderRepository.save(serviceOrder);
        receivableService.createReceivable(serviceOrder);

        return serviceOrder;
    }

    @Transactional
    public ServiceOrder cancelServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service order not found"));

        if (serviceOrder.getServiceOrderStatus() == ServiceOrderStatus.CANCELED) throw new ServiceOrderStatusException("Service order is already canceled");

        if (serviceOrder.getServiceOrderStatus() == ServiceOrderStatus.COMPLETED) {
            receivableService.cancelReceivable(serviceOrder);
        }

        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.CANCELED);
        serviceOrder.setCanceledAt(LocalDateTime.now());

        serviceOrderRepository.save(serviceOrder);

        return serviceOrder;
    }

    @Transactional
    public ServiceOrder updateServiceOrder(UpdateServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Service order not found"));

        if (request.motorcycleId() != null) {
            Motorcycle motorcycle = motorcycleRepository.findById(request.motorcycleId())
                    .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found"));
            if (motorcycle.getCustomer() == null) throw new MotorcycleNotAssignedException();
            serviceOrder.setMotorcycle(motorcycle);
        }

        if (request.sellerId() != null) {
            Employee seller = employeeRepository.findById(request.sellerId())
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
            if (seller.getRole() != EmployeeRole.SELLER && seller.getRole() != EmployeeRole.MANAGER) throw new EntityNotFoundException("Employee is not a seller or manager");
            serviceOrder.setSeller(seller);
        }

        if (request.mechanicId() != null) {
            Employee mechanic = employeeRepository.findById(request.mechanicId())
                    .orElseThrow(() -> new EntityNotFoundException("Mechanic not found"));
            if (mechanic.getRole() != EmployeeRole.MECHANIC) throw new EntityNotFoundException("Employee is not a mechanic");
            serviceOrder.setMechanic(mechanic);
        }

        if (request.products() != null) {
            serviceOrder.getProducts().clear();
            List<ServiceOrderProduct> products = request.products().stream()
                    .map(product -> serviceOrderProductService.addProduct(product, serviceOrder.getId()))
                    .toList();

            serviceOrder.getProducts().addAll(products);
        }

        if (request.description() != null) serviceOrder.setDescription(request.description());
        if (request.laborPrice() != null) serviceOrder.setLaborPrice(request.laborPrice());

        if (request.products() != null || request.laborPrice() != null) {
            BigDecimal totalCost = serviceOrder.getProducts().stream()
                    .map(ServiceOrderProduct::getFinalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).add(serviceOrder.getLaborPrice());

            serviceOrder.setTotalCost(totalCost);
        }

        serviceOrder.setUpdatedAt(LocalDateTime.now());
        serviceOrderRepository.save(serviceOrder);

        return serviceOrder;
    }

}
