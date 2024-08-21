package com.matteusmoreno.moto_manager.address.mapper;

import com.matteusmoreno.moto_manager.client.via_cep.ViaCepResponse;
import com.matteusmoreno.moto_manager.address.entity.Address;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AddressMapper {

    public Address mapToAddressForCreation(ViaCepResponse viaCepResponse, String number, String complement) {
        return Address.builder()
                .zipcode(viaCepResponse.cep())
                .city(viaCepResponse.localidade())
                .neighborhood(viaCepResponse.bairro())
                .state(viaCepResponse.uf())
                .street(viaCepResponse.logradouro())
                .complement(complement)
                .number(number)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
