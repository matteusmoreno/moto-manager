package com.matteusmoreno.moto_manager.serice_order.service_order_product.service;

import com.matteusmoreno.moto_manager.serice_order.service_order_product.entity.ServiceOrderProduct;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.mapper.ServiceOrderProductMapper;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.repository.ServiceOrderProductRepository;
import com.matteusmoreno.moto_manager.serice_order.service_order_product.request.CreateServiceOrderProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceOrderProductService {

    private final ServiceOrderProductRepository serviceOrderProductRepository;
    private final ServiceOrderProductMapper serviceOrderProductMapper;

    @Autowired
    public ServiceOrderProductService(ServiceOrderProductRepository serviceOrderProductRepository, ServiceOrderProductMapper serviceOrderProductMapper) {
        this.serviceOrderProductRepository = serviceOrderProductRepository;
        this.serviceOrderProductMapper = serviceOrderProductMapper;
    }

    @Transactional
    public ServiceOrderProduct addProduct(CreateServiceOrderProductRequest request) {
        ServiceOrderProduct serviceOrderProduct = serviceOrderProductMapper.mapToServiceOrderProductForCreation(request);
        serviceOrderProductRepository.save(serviceOrderProduct);

        return serviceOrderProduct;
    }
}
