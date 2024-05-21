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
import com.github.drug_store_be.web.DTO.Cart.CartResponse;
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

    //장바구니 조회
    public List<CartResponse> findAllCarts(int userId) {
        //사용자 조회
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //사용자 장바구니 조회
        List<Cart> carts = cartJpa.findAllByUser(user);
        if (carts.isEmpty()) {
            throw new RuntimeException("No cart items found");
        }

        // 장바구니 응답 객체 생성
        List<CartResponse> cartResponses = carts.stream()
                .map(cart -> {
                    // 장바구니에 대한 상품 및 옵션 정보 조회
                    ProductPhoto productPhoto = productPhotoJpa.findById(cart.getOptions().getProduct().getProductId())
                            .orElseThrow(() -> new RuntimeException("Product photo not found"));

                    // CartResponse 객체 생성
                    return CartResponse.builder()
                            .cartId(cart.getCartId())
                            .productId(cart.getOptions().getProduct().getProductId())
                            .productPhotoId(productPhoto.getProductPhotoId())
                            .brand(cart.getOptions().getProduct().getBrand())
                            .productName(cart.getOptions().getProduct().getProductName())
                            .optionId(cart.getOptions().getOptionsId())
                            .quantity(cart.getQuantity())
                            .price(cart.getOptions().getProduct().getPrice())
                            .build();
                })
                .collect(Collectors.toList());

        return cartResponses;
    }
}

