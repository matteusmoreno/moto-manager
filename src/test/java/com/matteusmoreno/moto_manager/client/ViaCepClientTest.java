package com.matteusmoreno.moto_manager.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("ViaCep Client Tests")
class ViaCepClientTest {

    @Autowired
    private ViaCepClient viaCepClient;

    @Test
    @DisplayName("Should consume the Viacep API and return an address correctly")
    void shouldConsumeTheViacepApiAndReturnAnAddressCorrectly() {
        String zipcode = "01001000";

        ViaCepResponse result = viaCepClient.getAddressByZipcode(zipcode);

        assertAll(
                () -> assertEquals("01001-000", result.cep()),
                () -> assertEquals("Praça da Sé", result.logradouro()),
                () -> assertEquals("Sé", result.bairro()),
                () -> assertEquals("São Paulo", result.localidade()),
                () -> assertEquals("SP", result.uf())
        );
    }
}