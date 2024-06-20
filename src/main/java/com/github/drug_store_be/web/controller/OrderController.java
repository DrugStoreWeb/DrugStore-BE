package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.service.OrderService;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.order.ProductRegisterDto;
import com.github.drug_store_be.web.DTO.pay.PayRequestDto;
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
    @PostMapping("/cart-to-order")
    public ResponseDto cartToOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return orderService.cartToOrder(customUserDetails);
    }

    @Operation(summary= "주문에서 결제로 넘어가기")
    @PutMapping("/order-to-pay")
    public ResponseDto orderToPay(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @RequestBody PayRequestDto payRequestDto){
        return orderService.orderToPay(customUserDetails, payRequestDto);
    }
}
