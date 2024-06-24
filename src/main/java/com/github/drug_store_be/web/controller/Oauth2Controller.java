package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.auth.Oauth2Service;
import com.github.drug_store_be.web.DTO.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j
public class Oauth2Controller {
    private final Oauth2Service oauth2Service;
    @GetMapping("/kakao")
    public void redirectKakaoAuth(HttpServletResponse response) {
        oauth2Service.redirectKakaoAuth(response);

    }

    @GetMapping("/kakao/callback")
    public ResponseDto kakaoCallback(@RequestParam String code, HttpServletResponse httpServletResponse) {
        String token = oauth2Service.callBack(code);
        httpServletResponse.setHeader("token",token);
        log.info("토큰: "+ token);
        return new ResponseDto(HttpStatus.OK.value(),"Login Success");
    }
}
