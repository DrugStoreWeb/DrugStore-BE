package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.cart.CartService;
import com.github.drug_store_be.web.DTO.Cart.CartRequest;
import com.github.drug_store_be.web.DTO.Cart.CartResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/myCart")
    public ResponseDto getCartItems(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        int userId = customUserDetails.getUserId();
        List<CartResponse> cartItems = cartService.findAllCarts(userId);
        return new ResponseDto(HttpStatus.OK.value(), "Cart items retrieved successfully", cartItems);
    }

    @PostMapping("/add")
    public ResponseDto addCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @RequestBody CartRequest cartRequest) {
        return cartService.addCartItem(customUserDetails, cartRequest);
    }

/*    @PutMapping("/update")
    public ResponseDto updateCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @RequestBody CartRequest cartRequest){
        return cartService.updateCartItem(customUserDetails, cartRequest);
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseDto removeCartItem(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @PathVariable int cartId){
        return cartService.removeCartItem(customUserDetails, cartId);
    }

    @DeleteMapping("/empty")
    public ResponseDto clearCart(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return cartService.clearCart(customUserDetails);
    }*/

}
