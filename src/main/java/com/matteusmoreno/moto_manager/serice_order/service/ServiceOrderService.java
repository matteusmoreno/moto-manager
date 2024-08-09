package com.matteusmoreno.moto_manager.serice_order.service;

import com.matteusmoreno.moto_manager.serice_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.serice_order.mapper.ServiceOrderMapper;
import com.matteusmoreno.moto_manager.serice_order.repository.ServiceOrderRepository;
import com.matteusmoreno.moto_manager.serice_order.request.CreateServiceOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
