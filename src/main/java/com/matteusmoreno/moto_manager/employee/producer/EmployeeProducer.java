package com.matteusmoreno.moto_manager.employee.producer;

import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.producer.employee_request.CreateEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.producer.employee_request.EnableAndDisableEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.producer.employee_request.UpdateEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EmployeeProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static final String EXCHANGE = "employee.email.exchange";

    public void publishCreateEmployeeEmail(Employee employee, CreateEmployeeRequest createEmployeeRequest) {
        CreateEmailEmployeeRequest request = new CreateEmailEmployeeRequest(employee, createEmployeeRequest);
        rabbitTemplate.convertAndSend(EXCHANGE, "employee.created", request);
    }

    public void publishUpdateEmployeeEmail(Employee employee) {
         UpdateEmailEmployeeRequest request = new UpdateEmailEmployeeRequest(employee);
        rabbitTemplate.convertAndSend(EXCHANGE, "employee.updated", request);
    }

    public void publishDisableEmployeeEmail(Employee employee) {
        EnableAndDisableEmailEmployeeRequest request = new EnableAndDisableEmailEmployeeRequest(employee);
        rabbitTemplate.convertAndSend(EXCHANGE, "employee.disabled", request);
    }

    public void publishEnableEmployeeEmail(Employee employee) {
        EnableAndDisableEmailEmployeeRequest request = new EnableAndDisableEmailEmployeeRequest(employee);
        rabbitTemplate.convertAndSend(EXCHANGE, "employee.enabled", request);
    }
}
