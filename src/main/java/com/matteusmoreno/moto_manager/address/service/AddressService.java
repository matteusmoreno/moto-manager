package com.matteusmoreno.moto_manager.address.service;

import com.matteusmoreno.moto_manager.address.mapper.AddressMapper;
import com.matteusmoreno.moto_manager.address.repository.AddressRepository;
import com.matteusmoreno.moto_manager.client.ViaCepClient;
import com.matteusmoreno.moto_manager.client.ViaCepResponse;
import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.exception.InvalidZipcodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final ViaCepClient viaCepClient;

    @Autowired
    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper, ViaCepClient viaCepClient) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.viaCepClient = viaCepClient;
    }

    @Transactional
    public Address createAddress(String zipcode, String number, String complement) {

        if (addressRepository.existsByZipcodeAndNumber(zipcode, number)) {
            return addressRepository.findByZipcodeAndNumber(zipcode, number);
        }

        ViaCepResponse viaCepResponse = viaCepClient.getAddressByZipcode(zipcode);

        if (viaCepResponse.localidade() == null) throw new InvalidZipcodeException();

        Address address = addressMapper.setAddressAttributes(viaCepResponse, number, complement);
        addressRepository.save(address);

        return address;
    }
}
