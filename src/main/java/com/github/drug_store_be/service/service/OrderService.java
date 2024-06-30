package com.github.drug_store_be.service.service;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.cart.CartRepository;
import com.github.drug_store_be.repository.coupon.Coupon;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsRepository;
import com.github.drug_store_be.repository.order.Orders;
import com.github.drug_store_be.repository.order.OrdersRepository;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.user.User;

import com.github.drug_store_be.repository.user.UserRepository;
import com.github.drug_store_be.repository.userCoupon.UserCoupon;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.*;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.order.OrderCouponResponseDto;
import com.github.drug_store_be.web.DTO.order.OrderProductResponseDto;
import com.github.drug_store_be.web.DTO.order.OrderResponseDto;
import com.github.drug_store_be.web.DTO.pay.PayRequestDto;
import com.github.drug_store_be.web.DTO.pay.OptionQuantityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrdersRepository ordersRepository;
    private final OptionsRepository optionsRepository;

    @Transactional
    public ResponseDto cartToOrder(CustomUserDetails customUserDetails) {
        User user= userRepository.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("아이디가  "+ customUserDetails.getUserId() +"인 유저를 찾을 수 없습니다."));
        List<Cart> cartList= cartRepository.findByUserId(user.getUserId());


        //재고 예외처리 method
        List<OptionQuantityDto> optionQuantityList =  cartList.stream().map(c-> new OptionQuantityDto(c.getOptions().getOptionsId(), c.getQuantity()))
                        .collect(Collectors.toList());
        exceptionCheck(optionQuantityList);


        if(!cartList.isEmpty()) {
            //save order JPA
            LocalDate orderAt = LocalDate.now();

            //UUID사용해서 짧고 고유한 주문번호 만들기
            String ordersNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 16);



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

    //주문에서 결제로
    @Transactional
    @CacheEvict(value = "productDetails",allEntries = true)
    public ResponseDto orderToPay(CustomUserDetails customUserDetails, PayRequestDto payRequestDto) {
        List<OptionQuantityDto> optionQuantityList= payRequestDto.getOptionQuantityDto();
        //재고 예외처리
        exceptionCheck(optionQuantityList);

        //사용자가 가진 돈이 주문하려는 금액보다 적으면 예외처리
        User user = userRepository.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NotFoundException("아이디가  " + customUserDetails.getUserId() + "인 유저를 찾을 수 없습니다."));
        Integer totalPrice = payRequestDto.getTotalPrice();
        if (totalPrice > user.getMoney()) throw new NoMoneyException("You do not have enough money to order");

        try {

            //option stock 재고 차감
            optionStockChange(optionQuantityList);

            //user money 차감
            user.setMoney(user.getMoney() - totalPrice);
            userRepository.save(user);

            //장바구니에서 주문한 상품은 삭제
            deleteFromCart(user, optionQuantityList);

            //주문 만들기
            //save order JPA
            LocalDate orderAt = LocalDate.now();
            //UUID사용해서 짧고 고유한 주문번호 만들기
            String ordersNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            for (OptionQuantityDto o : optionQuantityList) {
                saveOrder(user, o.getOptionId(), ordersNumber, orderAt);
            }


            return new ResponseDto(HttpStatus.OK.value(), "주문 성공");
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "주문 실패");
        }
    }




    //재고처리
    private void exceptionCheck(List<OptionQuantityDto> optionQuantityDtoList)  throws SoldOutException, NotEnoughStockException, ProductStatusException{
        for(OptionQuantityDto o: optionQuantityDtoList){
            Options options= optionsRepository.findById(o.getOptionId())
                    .orElseThrow(()-> new NotFoundException("Cannot find option with ID"));
            int buyQuantity=  o.getQuantity();
            int optionQuantity= options.getStock();
            if(optionQuantity==0) throw new SoldOutException("This product is sold out");
            else if(buyQuantity>optionQuantity) throw new NotEnoughStockException("Not possible. Product: "+ options.getProduct().getProductName() + " Option: "+ options.getOptionsName() + "Only "+ optionQuantity +"products available.");
            else if(!options.getProduct().isProductStatus()) throw new ProductStatusException("This product is not available at the moment.");
        }
    }

    private void optionStockChange(List<OptionQuantityDto> optionQuantityDtoList) {
        for(OptionQuantityDto o: optionQuantityDtoList){
            int optionId = o.getOptionId();
            Options options= optionsRepository.findById(optionId)
                    .orElseThrow(()-> new NotFoundException("Cannot find option with ID"));
            int orignialOptionStock= options.getStock();
            int orderedStock= o.getQuantity();
            options.setStock(orignialOptionStock - orderedStock);
            optionsRepository.save(options);
        }
    }

    private void deleteFromCart(User user, List<OptionQuantityDto> optionQuantityDtoList) {
        for(OptionQuantityDto o: optionQuantityDtoList){
            Options options= optionsRepository.findById(o.getOptionId())
                    .orElseThrow(()-> new NotFoundException("Cannot find option with ID"));


            Cart cart= cartRepository.findByUserIdAndOptionId(user.getUserId(), options.getOptionsId())
                    .orElseThrow(() -> new NotFoundException("There is no product in cart with matching user and option."));

//            //장바구니 삭제를 위해서 먼저 order부터 삭제
//            Orders order= ordersRepository.findByCart(cart);
//            ordersRepository.delete(order);
            //finally delete cart
            cartRepository.delete(cart);
        }
    }




    public String saveOrder(User user, Integer optionId, String ordersNumber, LocalDate orderAt){

        Options options= optionsRepository.findById(optionId)
                .orElseThrow(()-> new NotFoundException("Cannot find option with ID"));

        Orders orders= Orders.builder()
                .user(user)
                .options(options)
                .ordersNumber(ordersNumber)
                .ordersAt(orderAt)
                .build();
        ordersRepository.save(orders);
        return "order saved successfully";
    }

    public List<OrderCouponResponseDto> makeOrderCouponsList(User user){
        List<Coupon> couponList= user.getUserCouponList().stream().map(UserCoupon::getCoupon).toList();
        List<OrderCouponResponseDto> orderCouponResponseDtoList= couponList.stream()
                .map(
                        (c)-> new OrderCouponResponseDto(
                                c.getCouponId(),
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
                    String productImg = c.getOptions().getProduct().getProductPhotoList().stream()
                            .filter(ProductPhoto::isPhotoType)
                            .map(ProductPhoto::getPhotoUrl)
                            .findFirst()
                            .orElse(null);

                    return new OrderProductResponseDto(
                            productImg,
                            c.getOptions().getProduct().getProductName(),
                            c.getOptions().getProduct().getBrand(),
                            c.getOptions().getOptionsId(),
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


