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
import org.springframework.http.HttpStatus;
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

                    List<String> productPhotoUrls = productPhotoJpa.findByProduct(product).stream()
                            .map(ProductPhoto::getPhotoUrl)
                            .collect(Collectors.toList());

                    return CartResponse.builder()
                            .cartId(cart.getCartId())
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .brand(product.getBrand())
                            .optionId(options.getOptionsId())
                            .quantity(cart.getQuantity())
                            .price(product.getPrice())
                            .productPhotoUrls(productPhotoUrls)
                            .productDiscount(product.getProductDiscount())
                            .finalPrice(product.getFinalPrice())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ResponseDto addCartItem(CustomUserDetails customUserDetails, CartRequest cartRequest) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Options options = optionsJpa.findById(cartRequest.getOptionsId())
                .orElseThrow(() -> new RuntimeException("Options not found"));

        List<Cart> existingCarts = cartJpa.findByUserAndOptions(user, options);
        if (!existingCarts.isEmpty()) {
            Cart existingCart = existingCarts.get(0);
            existingCart.setQuantity(existingCart.getQuantity() + 1);
            cartJpa.save(existingCart);
            return new ResponseDto(HttpStatus.OK.value(), "Cart item quantity increased by 1 successfully");
        }

        Cart cart = Cart.builder()
                .user(user)
                .options(options)
                .quantity(1)
                .build();
        cartJpa.save(cart);
        return new ResponseDto(HttpStatus.OK.value(), "Cart item added successfully");
    }

    public ResponseDto updateCartItem(CustomUserDetails customUserDetails, CartRequest cartRequest) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer cartId = cartRequest.getCartId();
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID must not be null for update");
        }

        Cart cart = cartJpa.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (cart.getUser().getUserId() != userId) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        int quantity = cartRequest.getQuantity();
        if (quantity <= 0) {
            cartJpa.delete(cart);
            return new ResponseDto(HttpStatus.OK.value(), "Cart item removed successfully");
        }
        cart.setQuantity(quantity);

        Integer optionId = cartRequest.getOptionsId();
        if (optionId != null) {
            Options options = optionsJpa.findById(optionId)
                    .orElseThrow(() -> new RuntimeException("Options not found"));
            cart.setOptions(options);
        }

        cartJpa.save(cart);
        return new ResponseDto(HttpStatus.OK.value(), "Cart item updated successfully");
    }

    //상품 삭제
    public ResponseDto removeCartItem(CustomUserDetails customUserDetails, int cartId) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cartToDelete = cartJpa.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new RuntimeException("Product not found in user's cart"));

        cartJpa.delete(cartToDelete);
        return new ResponseDto(HttpStatus.OK.value(), "Product deleted from cart successfully");
    }

    //장바구니 비우기
    public ResponseDto clearCart(CustomUserDetails customUserDetails) {
        int userId = customUserDetails.getUserId();

        List<Cart> userCarts = cartJpa.findAllByUser_UserId(userId);

        if (!userCarts.isEmpty()) {
            cartJpa.deleteAll(userCarts);
            return new ResponseDto(HttpStatus.OK.value(), "Cart cleared successfully");
        } else {
            throw new RuntimeException("Cart is already empty");
        }
    }
}