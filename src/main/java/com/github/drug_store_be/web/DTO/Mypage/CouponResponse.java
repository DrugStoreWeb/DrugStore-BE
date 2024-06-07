package com.github.drug_store_be.web.DTO.Mypage;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {
    private String couponName;
    private Integer couponDiscount;
}
