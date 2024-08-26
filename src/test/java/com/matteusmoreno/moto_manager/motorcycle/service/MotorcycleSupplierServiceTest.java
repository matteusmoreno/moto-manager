package com.matteusmoreno.moto_manager.motorcycle.service;

import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.response.CustomerDetailsResponse;
import com.matteusmoreno.moto_manager.exception.MotorcycleAlreadyExistsException;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.mapper.MotorcycleMapper;
import com.matteusmoreno.moto_manager.motorcycle.repository.MotorcycleRepository;
import com.matteusmoreno.moto_manager.motorcycle.request.CreateMotorcycleRequest;
import com.matteusmoreno.moto_manager.motorcycle.request.UpdateMotorcycleRequest;
import com.matteusmoreno.moto_manager.motorcycle.response.MotorcycleDetailsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Motorcycle Service tests")
class MotorcycleSupplierServiceTest {

    @Mock
    private MotorcycleRepository motorcycleRepository;

    @Mock
    private MotorcycleMapper motorcycleMapper;

    @InjectMocks
    private MotorcycleService motorcycleService;

    @Test
    @DisplayName("Should create a new motorcycle successfully")
    void shouldCreateANewMotorcycleSuccessfully() {
        CreateMotorcycleRequest request = new CreateMotorcycleRequest(MotorcycleBrand.HONDA, "Biz 100",
                MotorcycleColor.RED, "FRP7898", "2010/2011");

        Motorcycle motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED,
                "FRP7898", "2010/2011", null, LocalDateTime.now(), null, null, true);

        when(motorcycleMapper.mapToMotorcycleForCreation(request)).thenReturn(motorcycle);
        when(motorcycleRepository.existsByPlate(request.plate())).thenReturn(false);

        Motorcycle result = motorcycleService.createMotorcycle(request);

        verify(motorcycleRepository, times(1)).existsByPlate(request.plate());
        verify(motorcycleMapper, times(1)).mapToMotorcycleForCreation(request);
        verify(motorcycleRepository, times(1)).save(result);

        assertNotNull(result.getId());
        assertEquals(request.brand(), result.getBrand());
        assertEquals(request.color(), result.getColor());
        assertEquals(request.model(), result.getModel());
        assertEquals(request.plate(), result.getPlate());
        assertEquals(request.year(), result.getYear());
        assertNull(result.getCustomer());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertNull(result.getDeletedAt());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("Should return MotorcycleAlreadyExistsException if the license plate exists in the database")
    void shouldReturnMotorcycleAlreadyExistsExceptionIfTheLicensePlateExistsInTheDatabase() {
        CreateMotorcycleRequest request = new CreateMotorcycleRequest(MotorcycleBrand.HONDA, "Biz 100",
                MotorcycleColor.RED, "FRP7898", "2010/2011");

        when(motorcycleRepository.existsByPlate(request.plate())).thenReturn(true);

        assertThrows(MotorcycleAlreadyExistsException.class, () -> motorcycleService.createMotorcycle(request));

        verify(motorcycleRepository, times(1)).existsByPlate(request.plate());
        verify(motorcycleMapper, times(0)).mapToMotorcycleForCreation(request);
        verify(motorcycleRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("Should return a paginated list of motorcycles")
    void shouldReturnAPaginatedListOfMotorcycles() {
        Motorcycle motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED,
                "FRP7898", "2010/2011", null, LocalDateTime.now(), null, null, true);

        Page<Motorcycle> motorcyclePage = new PageImpl<>(Collections.singletonList(motorcycle));
        Pageable pageable = Pageable.ofSize(10);

        when(motorcycleRepository.findAll(pageable)).thenReturn(motorcyclePage);

        Page<MotorcycleDetailsResponse> responsePage = motorcycleService.findAllMotorcycles(pageable);
        MotorcycleDetailsResponse response = responsePage.getContent().get(0);

        assertAll(
                () -> assertEquals(motorcycle.getId(), response.id()),
                () -> assertEquals(motorcycle.getBrand(), response.brand()),
                () -> assertEquals(motorcycle.getColor(), response.color()),
                () -> assertEquals(motorcycle.getModel(), response.model()),
                () -> assertEquals(motorcycle.getPlate(), response.plate()),
                () -> assertEquals(motorcycle.getYear(), response.year()),
                () -> assertEquals(motorcycle.getCreatedAt(), response.createdAt()),
                () -> assertEquals(motorcycle.getUpdatedAt(), response.updatedAt()),
                () -> assertEquals(motorcycle.getDeletedAt(), response.deletedAt()),
                () -> assertEquals(motorcycle.getDeletedAt(), response.deletedAt()),
                () -> assertEquals(motorcycle.getActive(), response.active())
        );
    }

    @Test
    @DisplayName("Should update a motorcycle successfully")
    void shouldUpdateAMotorcycleSuccessfully() {
        UUID motorcycleId = UUID.randomUUID();
        Motorcycle motorcycle = new Motorcycle(motorcycleId, MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED,
                "FRP7898", "2010/2011", null, LocalDateTime.now(), null, null, true);

        UpdateMotorcycleRequest request = new UpdateMotorcycleRequest(motorcycleId, MotorcycleBrand.YAMAHA, "Factor 125", MotorcycleColor.BLACK,
                "OPD1234", "2021");

        when(motorcycleRepository.findById(request.id())).thenReturn(Optional.of(motorcycle));

        Motorcycle result = motorcycleService.updateMotorcycle(request);

        verify(motorcycleRepository, times(1)).findById(request.id());
        verify(motorcycleRepository, times(1)).save(result);

        assertEquals(motorcycle.getId(), result.getId());
        assertEquals(request.brand(), result.getBrand());
        assertEquals(request.model(), result.getModel());
        assertEquals(request.color(), result.getColor());
        assertEquals(request.plate(), result.getPlate());
        assertEquals(request.year(), result.getYear());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if the motorcycle ID does not exist in the database")
    void shouldThrowEntityNotFoundExceptionIfTheMotorcycleIDDoesNotExistInTheDatabase() {
        UUID motorcycleId = UUID.randomUUID();
        UpdateMotorcycleRequest request = new UpdateMotorcycleRequest(motorcycleId, MotorcycleBrand.YAMAHA, "Factor 125", MotorcycleColor.BLACK,
                "OPD1234", "2021");

        assertThrows(EntityNotFoundException.class, () -> motorcycleService.updateMotorcycle(request));

        verify(motorcycleRepository, times(1)).findById(request.id());
        verify(motorcycleRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Should disable a motorcycle successfully")
    void shouldDisableAMotorcycleSuccessfully() {
        UUID motorcycleId = UUID.randomUUID();
        Motorcycle motorcycle = new Motorcycle(motorcycleId, MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED,
                "FRP7898", "2010/2011", null, LocalDateTime.now(), null, null, true);

        when(motorcycleRepository.findById(motorcycleId)).thenReturn(java.util.Optional.of(motorcycle));

       motorcycleService.disableMotorcycle(motorcycleId);

       verify(motorcycleRepository, times(1)).findById(motorcycleId);
       verify(motorcycleRepository, times(1)).save(motorcycle);

       assertFalse(motorcycle.getActive());
       assertNotNull(motorcycle.getDeletedAt());
    }

    @Test
    @DisplayName("Should enable a motorcycle successfully")
    void shouldEnableAMotorcycleSuccessfully() {
        UUID motorcycleId = UUID.randomUUID();
        Motorcycle motorcycle = new Motorcycle(motorcycleId, MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED,
                "FRP7898", "2010/2011", null, LocalDateTime.now(), null, null, false);

        when(motorcycleRepository.findById(motorcycleId)).thenReturn(Optional.of(motorcycle));

        Motorcycle result = motorcycleService.enableMotorcycle(motorcycleId);

        verify(motorcycleRepository, times(1)).findById(motorcycleId);
        verify(motorcycleRepository, times(1)).save(motorcycle);

        assertTrue(result.getActive());
        assertNull(result.getDeletedAt());
        assertNotNull(result.getUpdatedAt());
    }

}