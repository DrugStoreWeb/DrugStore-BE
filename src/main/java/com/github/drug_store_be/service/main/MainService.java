package com.github.drug_store_be.service.main;

import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;
import com.github.drug_store_be.web.DTO.ResponseDto;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class MainService {
    //정렬+광고
    public ResponseDto mainpage(String sortBy, Pageable pageable) {
    }

    //페이징+정렬
    public ResponseDto CategoryPage(String category, Pageable pageable) {
    }

    //페이징+정렬+검색
    public ResponseDto findPage(String keyword, Pageable pageable) {
    }

    MainPageResponse toMainpageResponseDto(productListQueryDto pld) {
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
