package com.matteusmoreno.moto_manager.service;

import com.matteusmoreno.moto_manager.client.ViaCepClient;
import com.matteusmoreno.moto_manager.client.ViaCepResponse;
import com.matteusmoreno.moto_manager.entity.Address;
import com.matteusmoreno.moto_manager.mapper.AddressMapper;
import com.matteusmoreno.moto_manager.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

        ViaCepResponse viaCepResponse = viaCepClient.getAddressByZipcode(zipcode);

        Address address = addressMapper.setAddressAttributes(viaCepResponse, number, complement);
        addressRepository.save(address);

        return address;
    }
}
