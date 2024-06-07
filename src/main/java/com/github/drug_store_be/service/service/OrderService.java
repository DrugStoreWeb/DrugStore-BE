package com.github.drug_store_be.service.service;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.cart.CartJpa;
import com.github.drug_store_be.repository.coupon.Coupon;
import com.github.drug_store_be.repository.order.Orders;
import com.github.drug_store_be.repository.order.OrdersJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userCoupon.UserCoupon;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.EmptyException;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.order.OrderCouponResponseDto;
import com.github.drug_store_be.web.DTO.order.OrderProductResponseDto;
import com.github.drug_store_be.web.DTO.order.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserJpa userJpa;
    private final CartJpa cartJpa;
    private final OrdersJpa ordersJpa;

    @Transactional
    public ResponseDto cartToOrder(CustomUserDetails customUserDetails) {
        User user= userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("아이디가  "+ customUserDetails.getUserId() +"인 유저를 찾을 수 없습니다."));
        List<Cart> cartList= cartJpa.findByUserId(user.getUserId());
        if(!cartList.isEmpty()) {
            //save order JPA
            LocalDate orderAt = LocalDate.now();
            Random random = new Random();
            Integer randomNumber = random.nextInt(10000);

            String ordersNumber = user.getUserId().toString() + orderAt.getYear() + ":" + randomNumber.toString();

            for (Cart c : cartList) {
                saveOrder(user, c, ordersNumber, orderAt);
            }

            //order response DTO
            OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                    .userName(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .address(user.getAddress())
                    .ordersNumber(ordersNumber)
                    .ordersAt(orderAt)
                    .orderCouponList(makeOrderCouponsList(user))
                    .orderProductList(makeOrderProductsList(cartList))
                    .build();

            return new ResponseDto(HttpStatus.OK.value(), "order page show success", orderResponseDto);
        }else{
            throw new EmptyException("유저의 장바구니가 비었습니다.");
        }
    }
    public String saveOrder(User user, Cart cart, String ordersNumber, LocalDate orderAt){

        Orders orders= Orders.builder()
                .user(user)
                .cart(cart)
                .ordersNumber(ordersNumber)
                .ordersAt(orderAt)
                .build();
        ordersJpa.save(orders);
        return "order saved successfully";
    }

    public List<OrderCouponResponseDto> makeOrderCouponsList(User user){
        List<Coupon> couponList= user.getUserCouponList().stream().map(UserCoupon::getCoupon).toList();
        List<OrderCouponResponseDto> orderCouponResponseDtoList= couponList.stream()
                .map(
                        (c)-> new OrderCouponResponseDto(
                                c.getCouponName(),
                                c.getCouponDiscount()
                        )
                )
                .toList();

        return orderCouponResponseDtoList;
    }


    public List<OrderProductResponseDto> makeOrderProductsList(List<Cart> cartList) {

        List<OrderProductResponseDto> orderProductResponseDtoList = cartList.stream()
                .map(c -> {
                    String productPhotoUrl = c.getOptions().getProduct().getProductPhotoList().stream()
                            .filter(ProductPhoto::isPhotoType)
                            .map(ProductPhoto::getPhotoUrl)
                            .findFirst()
                            .orElse(null);

                    return new OrderProductResponseDto(
                            productPhotoUrl,
                            c.getOptions().getProduct().getProductName(),
                            c.getOptions().getProduct().getBrand(),
                            c.getOptions().getOptionsName(),
                            c.getOptions().getProduct().getPrice(),
                            c.getOptions().getProduct().getFinalPrice(),
                            c.getQuantity()
                    );
                })
                .collect(Collectors.toList());

        return orderProductResponseDtoList;
    }
}


