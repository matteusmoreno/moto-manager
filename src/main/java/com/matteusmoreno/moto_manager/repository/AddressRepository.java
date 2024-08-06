package com.matteusmoreno.moto_manager.repository;

import com.matteusmoreno.moto_manager.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByZipcodeAndNumber(String zipcode, String number);

    Address findByZipcodeAndNumber(String zipcode, String number);
}
