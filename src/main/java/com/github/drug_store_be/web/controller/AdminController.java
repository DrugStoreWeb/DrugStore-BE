package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.PrincipalDetails;
import com.github.drug_store_be.service.service.AdminService;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.order.ProductRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    @Operation(summary= "상품 등록")
    @PostMapping("/addProduct")
    public ResponseDto registerProduct(@AuthenticationPrincipal PrincipalDetails UserDetails,
                                       @RequestBody ProductRegisterDto productRegisterDto){
        return adminService.registerProduct(UserDetails, productRegisterDto );
    }

}
