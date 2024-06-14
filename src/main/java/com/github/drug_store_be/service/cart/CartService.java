package com.github.drug_store_be.service.cart;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.cart.CartRepository;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsRepository;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductRepository;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Cart.CartRequest;
import com.github.drug_store_be.web.DTO.Cart.CartResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {
    private final UserJpa userJpa;
    private final ProductRepository productRepository;
    private final OptionsRepository optionsRepository;
    private final CartRepository cartRepository;

    // 장바구니 조회
    public List<CartResponse> findAllCarts(CustomUserDetails customUserDetails) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Cart> carts = cartRepository.findAllByUserOrderByCartIdDesc(user);
        if (carts.isEmpty()) {
            throw new NotFoundException("No cart items found");
        }

        return carts.stream()
                .map(cart -> {
                    Options options = cart.getOptions();
                    Product product = options.getProduct();

                    String productPhotoUrl = product.getProductPhotoList().stream()
                            .filter(ProductPhoto::isPhotoType)
                            .findFirst()
                            .map(ProductPhoto::getPhotoUrl)
                            .orElse(null);

                    return CartResponse.builder()
                            .cartId(cart.getCartId())
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .brand(product.getBrand())
                            .optionId(options.getOptionsId())
                            .quantity(cart.getQuantity())
                            .price(product.getPrice())
                            .productPhotoUrl(productPhotoUrl)
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

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (!product.isProductStatus()){
            throw new IllegalArgumentException("Product is not available for sale");
        }

        Options options = optionsRepository.findById(cartRequest.getOptionsId())
                .orElseThrow(() -> new NotFoundException("Options not found"));

        if (!options.getProduct().getProductId().equals(product.getProductId())) {
            throw new NotFoundException("Options do not belong to the specified product");
        }

        int requestedQuantity = cartRequest.getQuantity();
        if (requestedQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Optional<Cart> existingCarts = cartRepository.findByUserAndOptions(user, options);
        if (!existingCarts.isEmpty()) {
            Cart existingCart = existingCarts.get();
            int totalQuantity = existingCart.getQuantity() + requestedQuantity;
            if (totalQuantity > options.getStock()) {
                throw new IllegalArgumentException("No stock available for the selected option");
            }
            existingCart.setQuantity(totalQuantity);
            cartRepository.save(existingCart);
            return new ResponseDto(HttpStatus.OK.value(), "Cart item quantity updated successfully");
        }

        if (requestedQuantity > options.getStock()) {
            throw new IllegalArgumentException("No stock available for the selected option");
        }

        Cart cart = Cart.builder()
                .user(user)
                .options(options)
                .quantity(cartRequest.getQuantity())
                .build();
        cartRepository.save(cart);
        return new ResponseDto(HttpStatus.OK.value(), "Cart item added successfully");
    }

    //장바구니 업데이트
    public ResponseDto updateCartItem(CustomUserDetails customUserDetails, CartRequest cartRequest) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Integer cartId = cartRequest.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        Integer optionId = cartRequest.getOptionsId();
        if (optionId != null) {
            Options options = optionsRepository.findById(optionId)
                    .orElseThrow(() -> new NotFoundException("Options not found"));
            cart.setOptions(options);
        }

        int quantity = cartRequest.getQuantity();
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive"); // 예외를 발생시켜 0 이하의 수량이 입력되지 않도록 합니다.
        }

        Product product = cart.getOptions().getProduct();

        if (!product.isProductStatus()){
            throw new IllegalArgumentException("Product is not available for sale");
        }

        Options currentOptions = cart.getOptions();
        if (currentOptions.getStock() < quantity) {
            throw new IllegalArgumentException("No stock available for the selected quantity");
        }

        cart.setQuantity(quantity);
        cartRepository.save(cart);

        return new ResponseDto(HttpStatus.OK.value(), "Cart item updated successfully");
    }

    //장바구니 삭제
    public ResponseDto removeCartItem(CustomUserDetails customUserDetails, int cartId) {
        int userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Cart cartToDelete = cartRepository.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new NotFoundException("Product not found in user's cart"));

        cartRepository.delete(cartToDelete);
        return new ResponseDto(HttpStatus.OK.value(), "Product deleted from cart successfully");
    }

    //장바구니 비우기
    public ResponseDto clearCart(CustomUserDetails customUserDetails) {
        Integer userId = customUserDetails.getUserId();

        List<Cart> userCarts = cartRepository.findByUserId(userId);

        if (!userCarts.isEmpty()) {
            cartRepository.deleteAll(userCarts);
            return new ResponseDto(HttpStatus.OK.value(), "Cart cleared successfully");
        } else {
            throw new NotFoundException("Cart is already empty");
        }
    }
}