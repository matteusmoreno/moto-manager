package com.matteusmoreno.moto_manager.motorcycle.response;

import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

@DisplayName("Motorcycle Details Response Tests")
class MotorcycleDetailsResponseTest {

    @Test
    @DisplayName("Should create a MotorcycleDetailsResponse successfully")
    void shouldCreateMotorcycleDetailsResponseSuccessfully() {
        Motorcycle motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "BIZ", MotorcycleColor.BLACK, "ABC1234", "2021", null, LocalDateTime.now(), null, null, true);

        MotorcycleDetailsResponse motorcycleDetailsResponse = new MotorcycleDetailsResponse(motorcycle);

        Assertions.assertAll(
                () -> Assertions.assertEquals(motorcycle.getId(), motorcycleDetailsResponse.id()),
                () -> Assertions.assertEquals(motorcycle.getBrand(), motorcycleDetailsResponse.brand()),
                () -> Assertions.assertEquals(motorcycle.getModel(), motorcycleDetailsResponse.model()),
                () -> Assertions.assertEquals(motorcycle.getColor(), motorcycleDetailsResponse.color()),
                () -> Assertions.assertEquals(motorcycle.getPlate(), motorcycleDetailsResponse.plate()),
                () -> Assertions.assertEquals(motorcycle.getYear(), motorcycleDetailsResponse.year()),
                () -> Assertions.assertEquals(motorcycle.getCreatedAt(), motorcycleDetailsResponse.createdAt()),
                () -> Assertions.assertEquals(motorcycle.getUpdatedAt(), motorcycleDetailsResponse.updatedAt()),
                () -> Assertions.assertEquals(motorcycle.getDeletedAt(), motorcycleDetailsResponse.deletedAt()),
                () -> Assertions.assertEquals(motorcycle.getActive(), motorcycleDetailsResponse.active())
        );
    }

}