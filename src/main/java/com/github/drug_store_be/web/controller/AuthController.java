package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.auth.AuthService;
import com.github.drug_store_be.web.DTO.Auth.EmailCheck;
import com.github.drug_store_be.web.DTO.Auth.Login;
import com.github.drug_store_be.web.DTO.Auth.NicknameCheck;
import com.github.drug_store_be.web.DTO.Auth.SignUp;
import com.github.drug_store_be.web.DTO.ResponseDto;
import io.swagger.models.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/sign-up")
    public ResponseDto signUp(@RequestBody SignUp signUpRequest){
        return authService.signUpResult(signUpRequest);
    }
    @PostMapping(value = "/nickname-check")
    public ResponseDto nickNameCheck(@RequestBody NicknameCheck nicknameCheck){
        return authService.nicknameCheckResult(nicknameCheck);
    }
    @PostMapping(value = "/email-check")
    public ResponseDto emailCheck(@RequestBody EmailCheck emailCheck){
        return authService.emailCheckResult(emailCheck);
    }

    @PostMapping(value = "/login")
    public ResponseDto login(@RequestBody Login loginRequest, HttpServletResponse httpServletResponse){
        String token = authService.login(loginRequest);
        httpServletResponse.setHeader("token", token);
        return new ResponseDto(HttpStatus.OK.value(),"Login Success");
    }
}
