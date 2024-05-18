package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.exceptions.CAuthenticationEntryPointException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exceptions")
public class ExceptionController {
    @GetMapping(value = "/entrypoint")
    public void entrypointException() {
        throw new CAuthenticationEntryPointException( "인증 과정에서 에러가 발생했습니다.");
    }

    @GetMapping(value = "/access-denied")
    public void accessdeniedException() {
        throw new AccessDeniedException("권한이 거부되었습니다.");
    }
}
