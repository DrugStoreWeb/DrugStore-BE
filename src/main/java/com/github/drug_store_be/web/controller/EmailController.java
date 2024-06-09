package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.auth.EmailService;
import com.github.drug_store_be.web.DTO.Auth.EmailAuthNumCheck;
import com.github.drug_store_be.web.DTO.Auth.EmailCheck;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/send")
    public ResponseDto emailSend(@RequestBody EmailCheck emailCheck){
        return emailService.emailSendResult(emailCheck.getEmail());
    }
    @PostMapping("/auth-num-check")
    public ResponseDto authNumCheck(@RequestBody EmailAuthNumCheck emailAuthNumCheck){
        return emailService.authNumCheckResult(emailAuthNumCheck);
    }

}
