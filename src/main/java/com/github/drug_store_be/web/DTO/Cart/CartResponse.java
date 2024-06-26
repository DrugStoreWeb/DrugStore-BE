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
    private String productImg;
    private String brand;
    private String productName;
    private String optionsName;
    private Integer optionsId;
    private Integer optionsPrice;
    private Integer quantity;
    private Integer price;
    private Integer productDiscount;
    private Integer finalPrice;
    private List<String> allOptionNames;
}
