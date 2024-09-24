package com.matteusmoreno.moto_manager.employee.producer;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.producer.employee_request.CreateEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.producer.employee_request.EnableAndDisableEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.producer.employee_request.UpdateEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Producer Tests")
class EmployeeProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EmployeeProducer employeeProducer;

    private Employee employee;
    private CreateEmployeeRequest createEmployeeRequest;

    @BeforeEach
    void setUp() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        createEmployeeRequest = new CreateEmployeeRequest("username", "password", "Employee", "employee@email.com","222.222.222-22","(99)999999999", LocalDate.of(2000, 2, 10), EmployeeRole.SELLER, "28994-675", "21", "Casa");
        employee = new Employee(UUID.randomUUID(), "username", "password", "Employee", "employee@email.com", "(99)999999999", LocalDate.of(2000, 2, 10), Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, address, LocalDateTime.now(), null, null, true);
    }

    @Test
    @DisplayName("Should publish create employee email successfully")
    void shouldPublishCreateEmployeeEmailSuccessfully() {
        CreateEmailEmployeeRequest request = new CreateEmailEmployeeRequest(employee, createEmployeeRequest);

        employeeProducer.publishCreateEmployeeEmail(employee, createEmployeeRequest);

        verify(rabbitTemplate, times(1)).convertAndSend(EmployeeProducer.EXCHANGE, "employee.created", request);
    }

    @Test
    @DisplayName("Should publish update employee email successfully")
    void shouldPublishUpdateEmployeeEmailSuccessfully() {
        employee.setUpdatedAt(LocalDateTime.now());
        UpdateEmailEmployeeRequest request = new UpdateEmailEmployeeRequest(employee);

        employeeProducer.publishUpdateEmployeeEmail(employee);

        verify(rabbitTemplate, times(1)).convertAndSend(EmployeeProducer.EXCHANGE, "employee.updated", request);
    }

    @Test
    @DisplayName("Should publish disable employee email successfully")
    void shouldPublishDisableEmployeeEmailSuccessfully() {
        employee.setUpdatedAt(LocalDateTime.now());
        EnableAndDisableEmailEmployeeRequest request = new EnableAndDisableEmailEmployeeRequest(employee);

        employeeProducer.publishDisableEmployeeEmail(employee);

        verify(rabbitTemplate, times(1)).convertAndSend(EmployeeProducer.EXCHANGE, "employee.disabled", request);
    }

    @Test
    @DisplayName("Should publish enable employee email successfully")
    void shouldPublishEnableEmployeeEmailSuccessfully() {
        employee.setUpdatedAt(LocalDateTime.now());
        EnableAndDisableEmailEmployeeRequest request = new EnableAndDisableEmailEmployeeRequest(employee);

        employeeProducer.publishEnableEmployeeEmail(employee);

        verify(rabbitTemplate, times(1)).convertAndSend(EmployeeProducer.EXCHANGE, "employee.enabled", request);
    }
}