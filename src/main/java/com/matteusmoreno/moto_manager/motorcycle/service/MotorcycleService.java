package com.matteusmoreno.moto_manager.motorcycle.service;

import com.matteusmoreno.moto_manager.exception.MotorcycleAlreadyExistsException;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.mapper.MotorcycleMapper;
import com.matteusmoreno.moto_manager.motorcycle.repository.MotorcycleRepository;
import com.matteusmoreno.moto_manager.motorcycle.request.CreateMotorcycleRequest;
import com.matteusmoreno.moto_manager.motorcycle.request.UpdateMotorcycleRequest;
import com.matteusmoreno.moto_manager.motorcycle.response.MotorcycleDetailsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Transactional
    public Motorcycle updateMotorcycle(UpdateMotorcycleRequest request) {
        Motorcycle motorcycle = motorcycleRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found."));

        if (request.brand() != null) motorcycle.setBrand(request.brand());
        if (request.model() != null) motorcycle.setModel(request.model());
        if (request.color() != null) motorcycle.setColor(request.color());
        if (request.plate() != null) motorcycle.setPlate(request.plate());
        if (request.year() != null) motorcycle.setYear(request.year());

        motorcycle.setUpdatedAt(LocalDateTime.now());
        motorcycleRepository.save(motorcycle);

        return motorcycle;
    }

    @Transactional
    public void disableMotorcycle(UUID id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found."));

        motorcycle.setActive(false);
        motorcycle.setDeletedAt(LocalDateTime.now());
        motorcycleRepository.save(motorcycle);
    }

    @Transactional
    public Motorcycle enableMotorcycle(UUID id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found."));

        motorcycle.setActive(true);
        motorcycle.setDeletedAt(null);
        motorcycle.setUpdatedAt(LocalDateTime.now());
        motorcycleRepository.save(motorcycle);

        return motorcycle;
    }
}
