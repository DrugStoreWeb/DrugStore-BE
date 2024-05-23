package com.github.drug_store_be.web.DTO.MainPage;

import lombok.Builder;

@Builder
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
}
