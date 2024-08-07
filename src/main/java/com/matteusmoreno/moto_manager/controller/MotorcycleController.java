package com.matteusmoreno.moto_manager.controller;

import com.matteusmoreno.moto_manager.entity.Motorcycle;
import com.matteusmoreno.moto_manager.request.CreateMotorcycleRequest;
import com.matteusmoreno.moto_manager.response.MotorcycleDetailsResponse;
import com.matteusmoreno.moto_manager.service.MotorcycleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/motorcycles")
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    @Autowired
    public MotorcycleController(MotorcycleService motorcycleService) {
        this.motorcycleService = motorcycleService;
    }

    @PostMapping("/create")
    public ResponseEntity<MotorcycleDetailsResponse> create(@RequestBody @Valid CreateMotorcycleRequest request, UriComponentsBuilder uriBuilder) {
        Motorcycle motorcycle = motorcycleService.createMotorcycle(request);
        URI uri = uriBuilder.path("/motorcycles/create/{id}").buildAndExpand(motorcycle.getId()).toUri();

        return ResponseEntity.created(uri).body(new MotorcycleDetailsResponse(motorcycle));
    }
}
