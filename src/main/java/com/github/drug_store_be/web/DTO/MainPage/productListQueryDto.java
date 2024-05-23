package com.github.drug_store_be.web.DTO.MainPage;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class productListQueryDto {
    private Integer product_id;
    private String product_name;
    private String brand_name;
    private Integer price;
    private Integer final_price;
    private String product_img;
    private boolean likes;
    private boolean best;
    private boolean sales;
    private Double product_sales; //상품별 판매율
    private Double review_avg; //상품별 리뷰평균
    private Integer product_like; //상품별 좋아요 개수

    public static MainPageResponse toMainpageResponseDto(productListQueryDto pld) {
        return MainPageResponse.builder()
                .product_id(pld.getProduct_id())
                .product_name(pld.getProduct_name())
                .brand_name(pld.getBrand_name())
                .price(pld.getPrice())
                .final_price(pld.getFinal_price())
                .product_img(pld.getProduct_img())
                .likes(pld.isLikes())
                .best(pld.isBest())
                .sales(pld.isSales())
                .build();
    }
}
