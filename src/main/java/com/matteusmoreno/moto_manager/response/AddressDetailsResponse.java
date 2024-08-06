package com.matteusmoreno.moto_manager.response;

import com.matteusmoreno.moto_manager.entity.Address;

import java.time.LocalDateTime;

public record AddressDetailsResponse(
        Long id,
        String zipcode,
        String city,
        String neighborhood,
        String state,
        String street,
        String number,
        String complement,
        LocalDateTime createdAt) {

    public AddressDetailsResponse(Address address) {
        this(
                address.getId(),
                address.getZipcode(),
                address.getCity(),
                address.getNeighborhood(),
                address.getState(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getCreatedAt());
    }
}
