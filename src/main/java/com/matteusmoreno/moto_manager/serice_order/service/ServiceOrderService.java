package com.matteusmoreno.moto_manager.serice_order.service;

import com.matteusmoreno.moto_manager.serice_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.serice_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.serice_order.mapper.ServiceOrderMapper;
import com.matteusmoreno.moto_manager.serice_order.repository.ServiceOrderRepository;
import com.matteusmoreno.moto_manager.serice_order.request.CreateServiceOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;

    @Autowired
    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository, ServiceOrderMapper serviceOrderMapper) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderMapper = serviceOrderMapper;
    }

    @Transactional
    public ServiceOrder createServiceOrder(CreateServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderMapper.mapToServiceOrderForCreation(request);
        serviceOrderRepository.save(serviceOrder);

        return serviceOrder;
    }

    @Transactional
    public ServiceOrder startServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service order not found"));

        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.IN_PROGRESS);
        serviceOrder.setStartedAt(LocalDateTime.now());

        serviceOrderRepository.save(serviceOrder);

        return serviceOrder;
    }

    @Transactional
    public ServiceOrder completeServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service order not found"));

        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.COMPLETED);
        serviceOrder.setCompletedAt(LocalDateTime.now());

        serviceOrderRepository.save(serviceOrder);

        return serviceOrder;
    }
}
