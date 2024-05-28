package com.github.drug_store_be.web.DTO.Cart;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Integer cartId;
    private Integer productId;
    private String productPhotoUrl;
    private String brand;
    private String productName;
    private Integer optionId;
    private Integer quantity;
    private Integer price;
    private Integer productDiscount;
    private Integer finalPrice;
}
