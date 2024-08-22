package com.matteusmoreno.moto_manager.client.email_sender;

import com.matteusmoreno.moto_manager.client.email_sender.customer_request.CreateEmailCustomerRequest;
import com.matteusmoreno.moto_manager.client.email_sender.customer_request.EnableAndDisableEmailCustomerRequest;
import com.matteusmoreno.moto_manager.client.email_sender.customer_request.UpdateEmailCustomerRequest;
import com.matteusmoreno.moto_manager.client.email_sender.employee_request.CreateEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.client.email_sender.employee_request.EnableAndDisableEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.client.email_sender.employee_request.UpdateEmailEmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-sender", url = "http://localhost:8081")
public interface MailSenderClient {

    //EMPLOYEE
    @PostMapping("/email/employee-creation")
    void employeeCreationEmail(@RequestBody @Valid CreateEmailEmployeeRequest request);

    @PostMapping("/email/employee-update")
    void employeeUpdateEmail(@RequestBody @Valid UpdateEmailEmployeeRequest request);

    @PostMapping("/email/employee-disable")
    void employeeDisableEmail(@RequestBody @Valid EnableAndDisableEmailEmployeeRequest request);

    @PostMapping("/email/employee-enable")
    void employeeEnableEmail(@RequestBody @Valid EnableAndDisableEmailEmployeeRequest request);

    //CUSTOMER
    @PostMapping("/email/customer-creation")
    void customerCreationEmail(@RequestBody @Valid CreateEmailCustomerRequest request);

    @PostMapping("/email/customer-update")
    void customerUpdateEmail(@RequestBody @Valid UpdateEmailCustomerRequest request);

    @PostMapping("/email/customer-disable")
    void customerDisableEmail(@RequestBody @Valid EnableAndDisableEmailCustomerRequest request);
}
