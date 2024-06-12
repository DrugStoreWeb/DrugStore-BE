package com.github.drug_store_be.service.service;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.cart.CartJpa;
import com.github.drug_store_be.repository.coupon.Coupon;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsJpa;
import com.github.drug_store_be.repository.order.Orders;
import com.github.drug_store_be.repository.order.OrdersJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userCoupon.UserCoupon;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.*;
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
    private final OptionsJpa optionsJpa;

    @Transactional
    public ResponseDto cartToOrder(CustomUserDetails customUserDetails) {
        User user= userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("아이디가  "+ customUserDetails.getUserId() +"인 유저를 찾을 수 없습니다."));
        List<Cart> cartList= cartJpa.findByUserId(user.getUserId());


        //재고 예외처리 method
        exceptionCheck(cartList);

        //사용자가 가진 돈이 주문하려는 금액보다 적으면 예외처리
        int totalPrice= caculateTotalPrice(cartList);
        if(totalPrice > user.getMoney()) throw new NoMoneyException("You do not have enough money to proceed to order");

        if(!cartList.isEmpty()) {
            //save order JPA
            LocalDate orderAt = LocalDate.now();
            Random random = new Random();
            Integer randomNumber = random.nextInt(10000);

            String ordersNumber = user.getUserId().toString() + orderAt.getYear() + ":" + randomNumber.toString();

            for (Cart c : cartList) {
                saveOrder(user, c, ordersNumber, orderAt);
            }
            //user money 차감
            user.setMoney(user.getMoney()-totalPrice);
            userJpa.save(user);
            //option stock 차감
            optionStockChange(cartList);

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

    private void optionStockChange(List<Cart> cartList) {
        for(Cart c: cartList){
            Options options= optionsJpa.findById(c.getOptions().getOptionsId())
                    .orElseThrow(()-> new NotFoundException("Cannot find option with ID"));
            int orignialOptionStock= options.getStock();
            int orderedStock= c.getQuantity();
            options.setStock(orignialOptionStock - orderedStock);
            optionsJpa.save(options);

        }
    }

    //주문하려는 상품의 총 가격
    private int caculateTotalPrice(List<Cart> cartList) {
        return cartList.stream()
                .mapToInt(c -> c.getOptions().getProduct().getFinalPrice() + c.getOptions().getOptionsPrice())
                .sum();
    }

    //재고처리
    private static void exceptionCheck(List<Cart> cartList)  throws SoldOutException, NotEnoughStockException, ProductStatusException{
        for(Cart c: cartList){
            int cartQuantity=  c.getQuantity();
            int productQuantity= c.getOptions().getStock();
            if(productQuantity==0) throw new SoldOutException("This product is sold out");
            else if(cartQuantity>productQuantity) throw new NotEnoughStockException("Not possible. Only "+ productQuantity +"products available.");
            else if(!c.getOptions().getProduct().isProductStatus()) throw new ProductStatusException("This product is not available at the moment.");
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


