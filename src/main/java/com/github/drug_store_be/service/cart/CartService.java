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
import com.github.drug_store_be.service.exceptions.CAuthenticationEntryPointException;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Cart.CartRequest;
import com.github.drug_store_be.web.DTO.Cart.CartResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.annotations.NotFound;
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
    public List<CartResponse> findAllCarts(CustomUserDetails customUserDetails) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Cart> carts = cartJpa.findAllByUser_UserId(userId);
        if (carts.isEmpty()) {
            throw new NotFoundException("No cart items found");
        }

        return carts.stream()
                .map(cart -> {
                    Options options = cart.getOptions();
                    Product product = options.getProduct();

                    List<String> productPhotoUrls = productPhotoJpa.findByProduct(product).stream()
                            .filter(ProductPhoto::isPhotoType)
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

    //장바구니 추가
    public ResponseDto addCartItem(CustomUserDetails customUserDetails, CartRequest cartRequest) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Product product = productJpa.findById(cartRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Options options = optionsJpa.findById(cartRequest.getOptionsId())
                .orElseThrow(() -> new NotFoundException("Options not found"));

        if (!options.getProduct().getProductId().equals(product.getProductId())) {
            throw new NotFoundException("Options do not belong to the specified product");
        }

        List<Cart> existingCarts = cartJpa.findByUserAndOptions(user, options);
        int totalQuantity = cartRequest.getQuantity();
        if (!existingCarts.isEmpty()) {
            Cart existingCart = existingCarts.get(0);
            totalQuantity += existingCart.getQuantity();
        }

        if (totalQuantity > options.getStock()) {
            throw new IllegalArgumentException("No stock available for the selected option");
        }

        if (!existingCarts.isEmpty()) {
            Cart existingCart = existingCarts.get(0);
            existingCart.setQuantity(existingCart.getQuantity() + 1);
            cartJpa.save(existingCart);
            return new ResponseDto(HttpStatus.OK.value(), "Cart item quantity increased by 1 successfully");
        }

        Cart cart = Cart.builder()
                .user(user)
                .options(options)
                .quantity(cartRequest.getQuantity())
                .build();
        cartJpa.save(cart);
        return new ResponseDto(HttpStatus.OK.value(), "Cart item added successfully");
    }

    //장바구니 업데이트
    public ResponseDto updateCartItem(CustomUserDetails customUserDetails, CartRequest cartRequest) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Integer cartId = cartRequest.getCartId();
        Cart cart = cartJpa.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        Integer optionId = cartRequest.getOptionsId();
        if (optionId != null) {
            Options options = optionsJpa.findById(optionId)
                    .orElseThrow(() -> new NotFoundException("Options not found"));
            cart.setOptions(options);
        }

        int quantity = cartRequest.getQuantity();
        if (quantity <= 0) {
            cartJpa.delete(cart);
            return new ResponseDto(HttpStatus.OK.value(), "Cart item removed successfully");
        }

        Options currentOptions = cart.getOptions();
        if (currentOptions.getStock() < quantity) {
            throw new IllegalArgumentException("No stock available for the selected quantity");
        }

        cart.setQuantity(quantity);
        cartJpa.save(cart);

        return new ResponseDto(HttpStatus.OK.value(), "Cart item updated successfully");
    }

    //장바구니 삭제
    public ResponseDto removeCartItem(CustomUserDetails customUserDetails, int cartId) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Cart cartToDelete = cartJpa.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new NotFoundException("Product not found in user's cart"));

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
            throw new NotFoundException("Cart is already empty");
        }
    }
}