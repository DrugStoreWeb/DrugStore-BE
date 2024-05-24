package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.service.OrderService;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.order.ProductRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    @Operation(summary= "장바구니에서 주문할 상품들 가져오기")
    @PostMapping("/cartToOrder")
    public ResponseDto cartToOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return orderService.cartToOrder(customUserDetails);
    }
}
