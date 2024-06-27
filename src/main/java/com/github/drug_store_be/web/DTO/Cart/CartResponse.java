package com.github.drug_store_be.web.DTO.Cart;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartResponse {
    private Integer cartId;
    private Integer productId;
    private String productName;
    private String brand;
    private Integer optionsId;
    private String optionsName; // 선택한 옵션명
    private List<String> allOptionsNames; // 모든 옵션명
    private Integer optionsPrice;
    private Integer quantity;
    private Integer price;
    private String productImg;
    private Integer productDiscount;
    private Integer finalPrice;
}