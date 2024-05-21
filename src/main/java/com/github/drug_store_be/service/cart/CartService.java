package com.github.drug_store_be.service.cart;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.cart.CartJpa;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.web.DTO.Cart.CartRequest;
import com.github.drug_store_be.web.DTO.Cart.CartResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {
    private final UserJpa userJpa;
    private final ProductJpa productJpa;
    private final ProductPhotoJpa productPhotoJpa;
    private final OptionsJpa optionsJpa;
    private final CartJpa cartJpa;

    // 장바구니 조회
    public List<CartResponse> findAllCarts(int userId) {
        List<Cart> carts = cartJpa.findAllByUser_UserId(userId);
        if (carts.isEmpty()) {
            throw new RuntimeException("No cart items found");
        }

        return carts.stream()
                .map(cart -> {
                    Options options = cart.getOptions();
                    Product product = options.getProduct();

                    return CartResponse.builder()
                            .cartId(cart.getCartId())
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            // 나머지 필요한 정보들을 가져옴
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ResponseDto addCartItem(CustomUserDetails customUserDetails, CartRequest cartRequest) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Options options = optionsJpa.findById(cartRequest.getOptionId())
                .orElseThrow(() -> new RuntimeException("Options not found"));

        Product product = options.getProduct(); // Option에서 Product를 가져옴

        List<Cart> existingCarts = cartJpa.findByUserAndOptions(user, options);
        if (!existingCarts.isEmpty()) {
            Cart existingCart = existingCarts.get(0);
            existingCart.setQuantity(existingCart.getQuantity() + cartRequest.getQuantity());
            cartJpa.save(existingCart);
            return new ResponseDto(200, "Cart item updated successfully");
        }

        Cart newCart = Cart.builder()
                .user(user)
                .options(options)
                .quantity(cartRequest.getQuantity())
                .build();

        cartJpa.save(newCart);
        return new ResponseDto(200, "Cart item added successfully");
    }
}