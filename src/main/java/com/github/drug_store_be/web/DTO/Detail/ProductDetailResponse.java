package com.github.drug_store_be.web.DTO.Detail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.drug_store_be.repository.product.Product;
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

//    public ProductDetailResponse(Product product, List<ProductImg> productImgs, Integer reviewCount, List<ProductOption> productOptions) {
//        this.productId = product.getProductId();
//        this.productName = product.getProductName();
//        this.sales = product.getProductDiscount();
//        this.price = product.getPrice();
//        this.finalPrice = product.getFinalPrice();
//        this.productImg = productImgs;
//        this.reviewCount = reviewCount;
//        this.reviewAvg = product.getReviewAvg();
//        this.best = product.isBest();
//        this.brandName = product.getBrand();
//        this.productOptions = productOptions;
//    }

    public static ProductDetailResponse createProductDetail(Product product, List<ProductImg> productImgs, Integer reviewCount, List<ProductOption> productOptions){
        return ProductDetailResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .sales(product.getProductDiscount())
                .price(product.getPrice())
                .finalPrice(product.getFinalPrice())
                .productImg(productImgs)
                .reviewCount(reviewCount)
                .reviewAvg(product.getReviewAvg())
                .best(product.isBest())
                .brandName(product.getBrand())
                .productOptions(productOptions)
                .build();
    }
}
