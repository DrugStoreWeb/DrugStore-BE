package com.github.drug_store_be.web.DTO.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private Integer optionsId;
}
