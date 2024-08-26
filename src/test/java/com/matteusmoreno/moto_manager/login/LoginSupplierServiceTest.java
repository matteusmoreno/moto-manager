package com.matteusmoreno.moto_manager.login;

import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("Login Service Tests")
class LoginSupplierServiceTest {

    @Mock
    private Jwt mockJwt;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private LoginService loginService;

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = new Employee(UUID.randomUUID(), "username", "password", "Employee", "employee@email.com", "(99)999999999", LocalDate.of(2000, 2, 10),
                Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, null, LocalDateTime.now(), null, null, true);
    }

    @Test
    @DisplayName("Should Login user Correctly")
    void shouldLoginUserCorrectly() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        when(employeeRepository.findByUsername(loginRequest.username())).thenReturn(employee);
        when(employeeRepository.existsByUsername(loginRequest.username())).thenReturn(true);
        when(passwordEncoder.matches(loginRequest.password(), employee.getPassword())).thenReturn(true);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);
        when(mockJwt.getTokenValue()).thenReturn("encoderResult");

        String jwtToken = loginService.login(loginRequest);

        assertNotNull(jwtToken);
    }

    @Test
    @DisplayName("Should return BadCredentialsException when username invalid")
    void shouldReturnBadCredentialsExceptionWhenUsernameIsInvalid() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        when(employeeRepository.existsByUsername(loginRequest.username())).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> loginService.login(loginRequest));

        assertEquals("User or Password is invalid!", exception.getMessage());
    }

    @Test
    @DisplayName("Should return BadCredentialsException when password is invalid")
    void shouldReturnBadCredentialsExceptionWhenPasswordIsInvalid() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        when(employeeRepository.findByUsername(loginRequest.username())).thenReturn(employee);
        when(employeeRepository.existsByUsername(loginRequest.username())).thenReturn(true);
        when(passwordEncoder.matches(loginRequest.password(), employee.getPassword())).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> loginService.login(loginRequest));

        assertEquals("User or Password is invalid!", exception.getMessage());
    }

    // IMPLEMENTAR TESTE PARA QUANDO O LOGIN FOR COM O ADMIN

}