package com.matteusmoreno.moto_manager.motorcycle.mapper;

import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.request.CreateMotorcycleRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Motorcycle Mapper Test")
class MotorcycleMapperTest {

    @Autowired
    private MotorcycleMapper motorcycleMapper;

    @Test
    @DisplayName("Should map CreateMotorcycleRequest to Motorcycle correctly")
    void shouldMapCreateMotorcycleRequestToMotorcycleCorrectly() {
        CreateMotorcycleRequest request = new CreateMotorcycleRequest(MotorcycleBrand.HONDA, "Biz 100",
                MotorcycleColor.RED, "FRP7898", "2010/2011");

        Motorcycle result = motorcycleMapper.mapToMotorcycleForCreation(request);

        assertAll(
                () -> assertEquals(request.brand(), result.getBrand()),
                () -> assertEquals(request.model().toUpperCase(), result.getModel()),
                () -> assertEquals(request.color(), result.getColor()),
                () -> assertEquals(request.plate(), result.getPlate()),
                () -> assertEquals(request.year(), result.getYear())
        );

        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertNull(result.getDeletedAt());
        assertTrue(result.getActive());
    }
}