package com.github.drug_store_be.web.DTO.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartRequest {
    private Integer cartId;
    private Integer optionsId;
    private Integer quantity;
}
