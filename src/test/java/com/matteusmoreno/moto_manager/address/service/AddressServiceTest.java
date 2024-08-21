package com.matteusmoreno.moto_manager.address.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.address.mapper.AddressMapper;
import com.matteusmoreno.moto_manager.address.repository.AddressRepository;
import com.matteusmoreno.moto_manager.client.via_cep.ViaCepClient;
import com.matteusmoreno.moto_manager.client.via_cep.ViaCepResponse;
import com.matteusmoreno.moto_manager.exception.InvalidZipcodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Address Service Tests")
class AddressServiceTest {

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private AddressService addressService;


    @Test
    @DisplayName("Should create a new address successfully")
    void shouldCreateANewAddressSuccessfully() {
        String zipcode = "01001-000";
        String number = "123";
        String complement = "Apartment 1";

        ViaCepResponse viaCepResponse = new ViaCepResponse("01001-000", "Praça da Sé",
                "Sé", "São Paulo", "SP");

        Address address = new Address(10L, "01001-000", "São Paulo", "Sé", "SP",
                "Praça da Sé", "123", "Apartament 1", LocalDateTime.now());

        when(addressRepository.existsByZipcodeAndNumber(zipcode, number)).thenReturn(false);
        when(viaCepClient.getAddressByZipcode(zipcode)).thenReturn(viaCepResponse);
        when(addressMapper.mapToAddressForCreation(viaCepResponse, number, complement)).thenReturn(address);

        Address result = addressService.createAddress(zipcode, number, complement);

        verify(addressRepository, times(1)).existsByZipcodeAndNumber(zipcode, number);
        verify(addressRepository, times(0)).findByZipcodeAndNumber(zipcode, number);
        verify(viaCepClient, times(1)).getAddressByZipcode(zipcode);
        verify(addressMapper, times(1)).mapToAddressForCreation(viaCepResponse, number, complement);
        verify(addressRepository, times(1)).save(result);

        assertAll(
                () -> assertEquals(address, result),
                () -> assertEquals(address.getZipcode(), result.getZipcode()),
                () -> assertEquals(address.getNumber(), result.getNumber()),
                () -> assertEquals(address.getComplement(), result.getComplement()),
                () -> assertEquals(address.getCity(), result.getCity()),
                () -> assertEquals(address.getState(), result.getState()),
                () -> assertEquals(address.getStreet(), result.getStreet())
        );
    }

    @Test
    @DisplayName("Should return an existing address if the same zipcode and number are provided")
    void shouldReturnAnExistingAddressIfTheSameZipcodeAndNumberAreProvided() {
        String zipcode = "01001-000";
        String number = "123";
        String complement = "Apartment 1";

        Address existingAddress = new Address(10L, "01001-000", "São Paulo", "Sé", "SP",
                "Praça da Sé", "123", "Apartament 1", LocalDateTime.now());

        when(addressRepository.existsByZipcodeAndNumber(zipcode, number)).thenReturn(true);
        when(addressRepository.findByZipcodeAndNumber(zipcode, number)).thenReturn(existingAddress);

        Address result = addressService.createAddress(zipcode, number, complement);

        verify(addressRepository, times(1)).existsByZipcodeAndNumber(zipcode, number);
        verify(addressRepository, times(1)).findByZipcodeAndNumber(zipcode, number);
        verify(viaCepClient, times(0)).getAddressByZipcode(zipcode);
        verify(addressMapper, times(0)).mapToAddressForCreation(any(), any(), any());
        verify(addressRepository, times(0)).save(result);

        assertSame(existingAddress, result);
        assertSame(existingAddress.getZipcode(), result.getZipcode());
        assertSame(existingAddress.getNumber(), result.getNumber());
        assertSame(existingAddress.getComplement(), result.getComplement());
        assertSame(existingAddress.getNeighborhood(), result.getNeighborhood());
        assertSame(existingAddress.getCity(), result.getCity());
        assertSame(existingAddress.getState(), result.getState());
        assertSame(existingAddress.getStreet(), result.getStreet());
        assertSame(existingAddress.getCreatedAt(), result.getCreatedAt());
        assertSame(existingAddress.getId(), result.getId());
    }

    @Test
    @DisplayName("Should return an InvalidZipcodeException if the zipcode is invalid")
    void shouldReturnAnInvalidZipcodeExceptionIfTheZipcodeIsInvalid() {
        String zipcode = "00000-000";
        String number = "123";
        String complement = "Apartment 1";

        ViaCepResponse viaCepResponse = new ViaCepResponse(null, null, null, null, null);

        when(addressRepository.existsByZipcodeAndNumber(zipcode, number)).thenReturn(false);
        when(viaCepClient.getAddressByZipcode(zipcode)).thenReturn(viaCepResponse);

        assertThrows(InvalidZipcodeException.class, () -> addressService.createAddress(zipcode, number, complement));

        verify(addressRepository, times(1)).existsByZipcodeAndNumber(zipcode, number);
        verify(addressRepository, times(0)).findByZipcodeAndNumber(zipcode, number);
        verify(viaCepClient, times(1)).getAddressByZipcode(zipcode);
        verify(addressMapper, times(0)).mapToAddressForCreation(any(), any(), any());
        verify(addressRepository, times(0)).save(any());
    }

}