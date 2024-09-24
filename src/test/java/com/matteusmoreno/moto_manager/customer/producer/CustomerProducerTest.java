package com.matteusmoreno.moto_manager.customer.producer;

import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.producer.customer_request.CreateEmailCustomerRequest;
import com.matteusmoreno.moto_manager.customer.producer.customer_request.EnableAndDisableEmailCustomerRequest;
import com.matteusmoreno.moto_manager.customer.producer.customer_request.UpdateEmailCustomerRequest;
import com.matteusmoreno.moto_manager.customer.request.CreateCustomerRequest;
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
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Producer Tests")
class CustomerProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CustomerProducer customerProducer;

    private Customer customer;
    public static final String EXCHANGE = "customer.email.exchange";

    @BeforeEach
    void setUp() {
        customer = new Customer(UUID.randomUUID(), "Name", "email@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(99)999999999", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);

    }

    @Test
    @DisplayName("Should publish create customer email successfully")
    void shouldPublishCreateCustomerEmailSuccessfully() {
        CreateEmailCustomerRequest request = new CreateEmailCustomerRequest(customer);

        customerProducer.publishCreateCustomerEmail(customer);

        verify(rabbitTemplate, times(1)).convertAndSend(EXCHANGE, "customer.created", request);
    }

    @Test
    @DisplayName("Should publish update customer email successfully")
    void shouldPublishUpdateCustomerEmailSuccessfully() {
        customer.setUpdatedAt(LocalDateTime.now());
        UpdateEmailCustomerRequest request = new UpdateEmailCustomerRequest(customer);

        customerProducer.publishUpdateCustomerEmail(customer);

        verify(rabbitTemplate, times(1)).convertAndSend(EXCHANGE, "customer.updated", request);
    }

    @Test
    @DisplayName("Should publish disable customer email successfully")
    void shouldPublishDisableCustomerEmailSuccessfully() {
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setActive(false);
        EnableAndDisableEmailCustomerRequest request = new EnableAndDisableEmailCustomerRequest(customer);

        customerProducer.publishDisableCustomerEmail(customer);

        verify(rabbitTemplate, times(1)).convertAndSend(EXCHANGE, "customer.disabled", request);
    }

    @Test
    @DisplayName("Should publish enable customer email successfully")
    void shouldPublishEnableCustomerEmailSuccessfully() {
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setActive(true);
        EnableAndDisableEmailCustomerRequest request = new EnableAndDisableEmailCustomerRequest(customer);

        customerProducer.publishEnableCustomerEmail(customer);

        verify(rabbitTemplate, times(1)).convertAndSend(EXCHANGE, "customer.enabled", request);
    }
}