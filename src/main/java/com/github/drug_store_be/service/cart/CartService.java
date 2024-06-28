package com.github.drug_store_be.service.cart;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.cart.CartRepository;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsRepository;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductRepository;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserRepository;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Cart.AddCartRequest;
import com.github.drug_store_be.web.DTO.Cart.CartResponse;
import com.github.drug_store_be.web.DTO.Cart.UpdateCartRequest;
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
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OptionsRepository optionsRepository;
    private final CartRepository cartRepository;

    // 장바구니 조회
    public List<CartResponse> findAllCarts(CustomUserDetails customUserDetails) {
        Integer userId = customUserDetails.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Cart> carts = cartRepository.findAllByUserOrderByCartIdDesc(user);
        if (carts.isEmpty()) {
            throw new NotFoundException("No cart items found");
        }

        return carts.stream()
                .map(cart -> {
                    Options options = cart.getOptions();
                    Product product = options.getProduct();

                    String productImg = product.getProductPhotoList().stream()
                            .filter(ProductPhoto::isPhotoType)
                            .findFirst()
                            .map(ProductPhoto::getPhotoUrl)
                            .orElse(null);

                    List<String> allOptionsNames = optionsRepository.findAllByProductProductId(product.getProductId())
                            .stream()
                            .map(Options::getOptionsName)
                            .collect(Collectors.toList());

                    return CartResponse.builder()
                            .cartId(cart.getCartId())
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .brand(product.getBrand())
                            .optionsId(options.getOptionsId())
                            .optionsName(options.getOptionsName()) // 선택한 옵션명
                            .allOptionsNames(allOptionsNames) // 모든 옵션명
                            .optionsPrice(options.getOptionsPrice())
                            .quantity(cart.getQuantity())
                            .price(product.getPrice())
                            .productImg(productImg)
                            .productDiscount(product.getProductDiscount())
                            .finalPrice(product.getFinalPrice())
                            .build();
                })
                .collect(Collectors.toList());
    }


    //장바구니 추가
    public ResponseDto addCartItem(CustomUserDetails customUserDetails, List<AddCartRequest> cartRequests) {
        Integer userId = customUserDetails.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        for (AddCartRequest cartRequest : cartRequests) {
            if (cartRequest.getProductId() == null || cartRequest.getOptionsId() == null) {
                throw new IllegalArgumentException("Product ID and Options ID must be provided");
            }

            Product product = productRepository.findById(cartRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));

            if (!product.isProductStatus()) {
                throw new IllegalArgumentException("Product is not available for sale: " + product.getProductId());
            }

            Options options = optionsRepository.findById(cartRequest.getOptionsId())
                    .orElseThrow(() -> new NotFoundException("Options not found"));

            if (!options.getProduct().getProductId().equals(product.getProductId())) {
                throw new NotFoundException("Options do not belong to the specified product");
            }

            Integer requestedQuantity = cartRequest.getQuantity();
            if (requestedQuantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }

            Optional<Cart> existingCart = cartRepository.findByUserAndOptions(user, options);
            if (existingCart.isPresent()) {
                Cart cart = existingCart.get();
                Integer totalQuantity = cart.getQuantity() + requestedQuantity;
                if (totalQuantity > options.getStock()) {
                    throw new IllegalArgumentException("No stock available for the selected option: " + options.getOptionsId());
                }
                cart.setQuantity(totalQuantity);
                cartRepository.save(cart);
            } else {
                if (requestedQuantity > options.getStock()) {
                    throw new IllegalArgumentException("No stock available for the selected option: " + options.getOptionsId());
                }

                Cart cart = Cart.builder()
                        .user(user)
                        .options(options)
                        .quantity(requestedQuantity)
                        .build();
                cartRepository.save(cart);
            }
        }

        return new ResponseDto(HttpStatus.OK.value(), "Cart items added successfully");
    }

    // 장바구니 업데이트
    public ResponseDto updateCartItem(CustomUserDetails customUserDetails, UpdateCartRequest cartRequest) {
        Integer userId = customUserDetails.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Integer cartId = cartRequest.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        Integer optionId = cartRequest.getOptionsId();
        Options options = null;
        if (optionId != null) {
            options = optionsRepository.findById(optionId)
                    .orElseThrow(() -> new NotFoundException("Options not found"));

            // 옵션 이름이 요청과 일치하는지 검증
            String requestedOptionsName = cartRequest.getOptionsName();
            if (!options.getOptionsName().equals(requestedOptionsName)) {
                throw new IllegalArgumentException("Options name does not match the given options ID");
            }

            // 동일한 productId와 optionsId를 가진 다른 항목이 있는지 확인
            Optional<Cart> existingCart = cartRepository.findByUserAndOptions(user, options);
            if (existingCart.isPresent() && !existingCart.get().getCartId().equals(cartId)) {
                // 이미 존재하는 항목이 있으면 수량을 업데이트하고 현재 항목을 삭제
                Cart existingCartItem = existingCart.get();
                Integer totalQuantity = existingCartItem.getQuantity() + cartRequest.getQuantity();
                if (totalQuantity > options.getStock()) {
                    throw new IllegalArgumentException("No stock available for the selected option: " + options.getOptionsId());
                }
                existingCartItem.setQuantity(totalQuantity);
                cartRepository.save(existingCartItem);
                cartRepository.delete(cart);
                return new ResponseDto(HttpStatus.OK.value(), "Cart item updated successfully");
            }

            cart.setOptions(options);
        } else {
            options = cart.getOptions();
        }

        // 옵션명이 요청에 포함되어 있는지 확인 (필수 필드)
        String optionsName = cartRequest.getOptionsName();
        if (optionsName == null || optionsName.isEmpty()) {
            throw new IllegalArgumentException("Options name must be provided");
        }

        // 옵션명이 해당 제품의 옵션명 리스트에 포함되어 있는지 확인
        List<String> validOptionNames = optionsRepository.findAllByProductProductId(options.getProduct().getProductId())
                .stream()
                .map(Options::getOptionsName)
                .collect(Collectors.toList());
        if (!validOptionNames.contains(optionsName)) {
            throw new IllegalArgumentException("Invalid options name for the given product");
        }

        // 기존 allOptionsNames 값을 저장
        List<String> originalAllOptionsNames = optionsRepository.findAllByProductProductId(options.getProduct().getProductId())
                .stream()
                .map(Options::getOptionsName)
                .collect(Collectors.toList());

        // 옵션명이 요청에 포함되어 있다면 업데이트
        options.setOptionsName(optionsName);
        optionsRepository.save(options);

        Integer quantity = cartRequest.getQuantity();
        if (quantity != null && quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (options.getStock() < quantity) {
            throw new IllegalArgumentException("No stock available for the selected quantity");
        }

        // 기존 장바구니 항목이 없으면 예외 발생
        if (options == null) {
            throw new NotFoundException("No existing cart item found for the given options");
        }

        cart.setQuantity(quantity);
        cartRepository.save(cart);

        // allOptionsNames 반환 (수정 전 값을 사용)
        return new ResponseDto(HttpStatus.OK.value(), "Cart item updated successfully", originalAllOptionsNames);
    }

    // 특정 제품의 모든 옵션명 조회
    public List<String> getAllOptionNamesByProductId(Integer productId) {
        return optionsRepository.findAllByProductProductId(productId)
                .stream()
                .map(Options::getOptionsName)
                .collect(Collectors.toList());
    }

    //장바구니 삭제
    public ResponseDto removeCartItem(CustomUserDetails customUserDetails, Integer cartId) {
        Integer userId = customUserDetails.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Cart cartToDelete = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        if (!cartToDelete.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("User does not have permission to delete this cart item");
        }

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