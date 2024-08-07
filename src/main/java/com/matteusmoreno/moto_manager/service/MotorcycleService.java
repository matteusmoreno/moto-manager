package com.matteusmoreno.moto_manager.service;

import com.matteusmoreno.moto_manager.entity.Motorcycle;
import com.matteusmoreno.moto_manager.exception.MotorcycleAlreadyExistsException;
import com.matteusmoreno.moto_manager.mapper.MotorcycleMapper;
import com.matteusmoreno.moto_manager.repository.MotorcycleRepository;
import com.matteusmoreno.moto_manager.request.CreateMotorcycleRequest;
import com.matteusmoreno.moto_manager.response.MotorcycleDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MotorcycleService {

    private final MotorcycleRepository motorcycleRepository;
    private final MotorcycleMapper motorcycleMapper;

    @Autowired
    public MotorcycleService(MotorcycleRepository motorcycleRepository, MotorcycleMapper motorcycleMapper) {
        this.motorcycleRepository = motorcycleRepository;
        this.motorcycleMapper = motorcycleMapper;
    }

    @Transactional
    public Motorcycle createMotorcycle(CreateMotorcycleRequest request) {
        Motorcycle motorcycle = motorcycleMapper.mapToMotorcycleForCreation(request);
        if (motorcycleRepository.existsByPlate(motorcycle.getPlate())) throw new MotorcycleAlreadyExistsException();
        motorcycleRepository.save(motorcycle);

        return motorcycle;
    }

    public Page<MotorcycleDetailsResponse> findAllMotorcycles(Pageable pageable) {
        return motorcycleRepository.findAll(pageable).map(MotorcycleDetailsResponse::new);
    }
}
