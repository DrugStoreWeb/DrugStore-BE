package com.github.drug_store_be.web.DTO.Cart;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Integer cartId;
    private Integer productId;
    private Integer productPhotoId;
    private String brand;
    private String productName;
    private Integer optionId;
    private Integer quantity;
    private Integer price;
}
