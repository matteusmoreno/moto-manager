package com.matteusmoreno.moto_manager.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-sender", url = "http://localhost:8081")
public interface MailSenderClient {

    @PostMapping("/email/send-email")
    void sendEmail(@RequestBody @Valid EmailSendRequest request);
}
