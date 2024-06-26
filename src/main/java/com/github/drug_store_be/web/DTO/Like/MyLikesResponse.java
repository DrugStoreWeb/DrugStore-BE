package com.github.drug_store_be.web.DTO.Like;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MyLikesResponse {
    private Integer productId;
    private String productName;
    private String productImg;
    private Integer price;
    private Integer finalPrice;
    private String brandName;
    private boolean likes;
}
