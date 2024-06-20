package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.auth.AuthService;
import com.github.drug_store_be.web.DTO.Auth.*;
import com.github.drug_store_be.web.DTO.ResponseDto;
import io.swagger.models.Response;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/sign-up")
    public ResponseDto signUp(@RequestPart("sign") @Valid SignUp signUpRequest
            ,@RequestPart("uploadFiles") MultipartFile multipartFiles){

        return authService.signUpResult(signUpRequest,multipartFiles);
    }

    @PostMapping(value = "/nickname") //노션에는 get?
    public ResponseDto nickNameCheck(@RequestBody NicknameCheck nicknameCheck){
        return authService.nicknameCheckResult(nicknameCheck);
    }
    @PostMapping(value = "/email") //노션에는 get?
    public ResponseDto emailCheck(@RequestBody EmailCheck emailCheck){
        return authService.emailCheckResult(emailCheck);
    }

    @PostMapping(value = "/login")
    public ResponseDto login(@RequestBody Login loginRequest, HttpServletResponse httpServletResponse){
        String token = authService.login(loginRequest);
        httpServletResponse.setHeader("token", token);
        return new ResponseDto(HttpStatus.OK.value(),"Login Success");
    }
    @PostMapping(value = "/find-email") //노션에는 get?
    public ResponseDto findEmail(@RequestBody FindEmail findEmail){

        return  authService.findEmailResult(findEmail);
    }
    @PutMapping(value = "/password")
    public ResponseDto changePassword(@RequestBody ChangePassword changePassword){
        return authService.changePasswordResult(changePassword);
    }
}
