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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseDto getCartItems(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) Integer optionId) {
        List<CartResponse> cartItems = cartService.findAllCarts(customUserDetails, optionId);
        return new ResponseDto(HttpStatus.OK.value(), "Cart items retrieved successfully", cartItems);
    }

    @PostMapping
    public ResponseDto addCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @RequestBody AddCartRequest addcartRequest) {
        return cartService.addCartItem(customUserDetails, addcartRequest);
    }

    @PutMapping
    public ResponseDto updateCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @RequestBody UpdateCartRequest updateCartRequest){
        return cartService.updateCartItem(customUserDetails, updateCartRequest);
    }

    @DeleteMapping("/{cartId}")
    public ResponseDto removeCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @PathVariable Integer cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID must be provided as part of the URL path");
        }
        return cartService.removeCartItem(customUserDetails, cartId);
    }

    @DeleteMapping("/empty")
    public ResponseDto clearCart(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return cartService.clearCart(customUserDetails);
    }
}
