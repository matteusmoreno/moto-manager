package com.matteusmoreno.moto_manager.customer.producer;

import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.producer.customer_request.CreateEmailCustomerRequest;
import com.matteusmoreno.moto_manager.customer.producer.customer_request.EnableAndDisableEmailCustomerRequest;
import com.matteusmoreno.moto_manager.customer.producer.customer_request.UpdateEmailCustomerRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CustomerProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static final String EXCHANGE = "customer.email.exchange";

    public void publishCreateCustomerEmail(Customer customer) {
        CreateEmailCustomerRequest request = new CreateEmailCustomerRequest(customer);
        rabbitTemplate.convertAndSend(EXCHANGE, "customer.created", request);
    }

    public void publishUpdateCustomerEmail(Customer customer) {
        UpdateEmailCustomerRequest request = new UpdateEmailCustomerRequest(customer);
        rabbitTemplate.convertAndSend(EXCHANGE, "customer.updated", request);
    }

    public void publishDisableCustomerEmail(Customer customer) {
        EnableAndDisableEmailCustomerRequest request = new EnableAndDisableEmailCustomerRequest(customer);
        rabbitTemplate.convertAndSend(EXCHANGE, "customer.disabled", request);
    }

    public void publishEnableCustomerEmail(Customer customer) {
        EnableAndDisableEmailCustomerRequest request = new EnableAndDisableEmailCustomerRequest(customer);
        rabbitTemplate.convertAndSend(EXCHANGE, "customer.enabled", request);
    }
}
