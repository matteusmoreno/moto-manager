package com.matteusmoreno.moto_manager.service;

import com.matteusmoreno.moto_manager.entity.Motorcycle;
import com.matteusmoreno.moto_manager.exception.MotorcycleAlreadyExistsException;
import com.matteusmoreno.moto_manager.mapper.MotorcycleMapper;
import com.matteusmoreno.moto_manager.repository.MotorcycleRepository;
import com.matteusmoreno.moto_manager.request.CreateMotorcycleRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
}
