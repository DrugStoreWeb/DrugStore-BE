package com.github.drug_store_be.web.DTO.Detail;

import com.github.drug_store_be.repository.like.LikesJpa;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class ProductDetailResponse {
    private Integer productId;
    private String productName;
    private Double sales;
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
