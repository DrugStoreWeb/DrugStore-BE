package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.cart.CartService;
import com.github.drug_store_be.web.DTO.Cart.AddCartRequest;
import com.github.drug_store_be.web.DTO.Cart.CartResponse;
import com.github.drug_store_be.web.DTO.Cart.UpdateCartRequest;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseDto getCartItems(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<CartResponse> cartItems = cartService.findAllCarts(customUserDetails);
        return new ResponseDto(HttpStatus.OK.value(), "Cart items retrieved successfully", cartItems);
    }

    @PostMapping
    public ResponseDto addCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @RequestBody List<AddCartRequest> cartRequests) {
        return cartService.addCartItem(customUserDetails, cartRequests);
    }

    @PutMapping
    public ResponseDto updateCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @RequestBody UpdateCartRequest updateCartRequest){
        return cartService.updateCartItem(customUserDetails, updateCartRequest);
    }

    @DeleteMapping("/{cartId}")
    public ResponseDto removeCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @PathVariable Integer cartId) {
        return cartService.removeCartItem(customUserDetails, cartId);
    }

    @DeleteMapping("/empty")
    public ResponseDto clearCart(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return cartService.clearCart(customUserDetails);
    }
}
