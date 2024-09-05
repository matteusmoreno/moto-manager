package com.matteusmoreno.moto_manager.address.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.address.repository.AddressRepository;
import com.matteusmoreno.moto_manager.client.via_cep.ViaCepClient;
import com.matteusmoreno.moto_manager.client.via_cep.ViaCepResponse;
import com.matteusmoreno.moto_manager.exception.InvalidZipcodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final ViaCepClient viaCepClient;

    @Autowired
    public AddressService(AddressRepository addressRepository, ViaCepClient viaCepClient) {
        this.addressRepository = addressRepository;
        this.viaCepClient = viaCepClient;
    }

    @Transactional
    public Address createAddress(String zipcode, String number, String complement) {

        if (addressRepository.existsByZipcodeAndNumber(zipcode, number)) {
            return addressRepository.findByZipcodeAndNumber(zipcode, number);
        }

        ViaCepResponse viaCepResponse = viaCepClient.getAddressByZipcode(zipcode);

        if (viaCepResponse.localidade() == null) throw new InvalidZipcodeException();

        Address address = Address.builder()
                .zipcode(viaCepResponse.cep())
                .city(viaCepResponse.localidade())
                .neighborhood(viaCepResponse.bairro())
                .state(viaCepResponse.uf())
                .street(viaCepResponse.logradouro())
                .complement(complement)
                .number(number)
                .createdAt(LocalDateTime.now())
                .build();
        addressRepository.save(address);

        return address;
    }
}
