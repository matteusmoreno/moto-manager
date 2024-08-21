package com.matteusmoreno.moto_manager.client.email_sender;

import com.matteusmoreno.moto_manager.client.email_sender.employee_request.CreateEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.client.email_sender.employee_request.EnableAndDisableEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.client.email_sender.employee_request.UpdateEmailEmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-sender", url = "http://localhost:8081")
public interface MailSenderClient {

    @PostMapping("/email/employee-creation")
    void employeeCreationEmail(@RequestBody @Valid CreateEmailEmployeeRequest request);

    @PostMapping("/email/employee-update")
    void employeeUpdateEmail(@RequestBody @Valid UpdateEmailEmployeeRequest request);

    @PostMapping("/email/employee-disable")
    void employeeDisableEmail(@RequestBody @Valid EnableAndDisableEmailEmployeeRequest request);

    @PostMapping("/email/employee-enable")
    void employeeEnableEmail(@RequestBody @Valid EnableAndDisableEmailEmployeeRequest request);
}
