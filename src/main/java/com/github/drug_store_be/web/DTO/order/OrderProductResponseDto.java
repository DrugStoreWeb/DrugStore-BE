package com.github.drug_store_be.web.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderProductResponseDto {
    private String productImg;
    private String productName;
    private String brand;
    private String optionName;
    private Integer price;
    private Integer finalPrice;
    private Integer quantity;
}
