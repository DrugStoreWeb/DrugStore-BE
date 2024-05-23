package com.github.drug_store_be.service.main;

import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;
import com.github.drug_store_be.web.DTO.ResponseDto;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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



    void PageSorting(String sortBy){
        List<productListQueryDto> productList = new ArrayList<>();
        switch (sortBy) {
            case "likes":
                Comparator<productListQueryDto> comparingLikesReverse = Comparator.comparing(productListQueryDto::getProduct_like, Comparator.reverseOrder());

                productList.stream()
                        .sorted(comparingLikesReverse)
                        .map(productListQueryDto::toMainpageResponseDto)
                        .collect(Collectors.toList());
                break;

            case "new":
                Comparator<productListQueryDto> comparingNewReverse = Comparator.comparing(productListQueryDto::getProduct_id, Comparator.reverseOrder());

                productList.stream()
                        .sorted(comparingNewReverse)
                        .map(productListQueryDto::toMainpageResponseDto)
                        .collect(Collectors.toList());
                break;
            case "price":
                Comparator<productListQueryDto> comparingPriceReverse = Comparator.comparing(productListQueryDto::getPrice, Comparator.reverseOrder());

                productList.stream()
                        .sorted(comparingPriceReverse)
                        .map(productListQueryDto::toMainpageResponseDto)
                        .collect(Collectors.toList());
                break;
            case "reviews":
                Comparator<productListQueryDto> comparingReviewsReverse = Comparator.comparing(productListQueryDto::getReview_avg, Comparator.reverseOrder());

                productList.stream()
                        .sorted(comparingReviewsReverse)
                        .map(productListQueryDto::toMainpageResponseDto)
                        .collect(Collectors.toList());
                break;
            case "sales":
                Comparator<productListQueryDto> comparingProductSalesReverse = Comparator.comparing(productListQueryDto::getProduct_sales, Comparator.reverseOrder());

                productList.stream()
                        .sorted(comparingProductSalesReverse)
                        .map(productListQueryDto::toMainpageResponseDto)
                        .collect(Collectors.toList());
                break;
        }
    }
}
