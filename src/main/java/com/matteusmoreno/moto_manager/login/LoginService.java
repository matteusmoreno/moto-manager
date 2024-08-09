package com.matteusmoreno.moto_manager.login;

import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;

@Service
public class LoginService {

    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public LoginService(JwtEncoder jwtEncoder, BCryptPasswordEncoder passwordEncoder, EmployeeRepository employeeRepository) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public String login(LoginRequest request) {

        Employee employee = employeeRepository.findByUsername(request.username());

        if (!employeeRepository.existsByUsername(request.username()) || verifyCorrectPassword(request, passwordEncoder, employee)) {
            throw new BadCredentialsException("User or Password is invalid!");
        }

        JwtClaimsSet claims = setJwtClaims(employee);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private static JwtClaimsSet setJwtClaims(Employee employee) {
        return JwtClaimsSet.builder()
                .issuer("MotoManager")
                .subject(employee.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(600L))
                .claim("userId", employee.getId())
                .claim("name", employee.getName())
                .claim("scope", employee.getRole())
                .build();
    }

    private static boolean verifyCorrectPassword(LoginRequest request, PasswordEncoder passwordEncoder, Employee employee) {
        if (Objects.equals(employee.getUsername(), "admin")) return false;
        return !passwordEncoder.matches(request.password(), employee.getPassword());
    }
}
