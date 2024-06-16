package com.github.drug_store_be.web.DTO.Mypage;

import com.github.drug_store_be.web.DTO.order.OrderCouponResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {
    private Integer money;

    private List<OrderCouponResponseDto> couponResponseList;

//    public CouponResponse(String couponName, Integer couponDiscount, Integer money) {
//        this.couponName = couponName;
//        this.couponDiscount = couponDiscount;
//        this.money = money;
//    }
}
