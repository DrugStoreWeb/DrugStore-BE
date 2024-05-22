package com.github.drug_store_be.web.DTO.Detail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.drug_store_be.repository.like.LikesJpa;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDetailResponse {
    private Integer productId;
    private String productName;
    private Integer sales;
    private Integer price;
    private Integer finalPrice;
    private List<ProductImg> productImg;
    private Integer reviewCount;
    private Double reviewAvg;
    private Boolean isLike;
    private Boolean best;
    private String brandName;
    private List<ProductOption> productOptions;

}
