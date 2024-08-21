package com.matteusmoreno.moto_manager.address.mapper;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.client.via_cep.ViaCepResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Address Mapper Tests")
class AddressMapperTest {

    @Autowired
    private AddressMapper addressMapper;

    @Test
    @DisplayName("Should map ViaCepResponse to Address correctly")
    void shouldMapViaCepResponseToAddressCorrectly() {
        String number = "223";
        String complement = "Armação Motos";
        ViaCepResponse viaCepResponse = new ViaCepResponse("28994-675", "Rua Alfredo Menezes",
                "Bacaxá(Bacaxá)", "Saquarema", "RJ");

        Address result = addressMapper.mapToAddressForCreation(viaCepResponse, number, complement);

        assertEquals(viaCepResponse.cep(), result.getZipcode());
        assertEquals(viaCepResponse.logradouro(), result.getStreet());
        assertEquals(viaCepResponse.bairro(), result.getNeighborhood());
        assertEquals(viaCepResponse.localidade(), result.getCity());
        assertEquals(viaCepResponse.uf(), result.getState());
        assertEquals(number, result.getNumber());
        assertEquals(complement, result.getComplement());
        assertNotNull(result.getCreatedAt());

    }
}