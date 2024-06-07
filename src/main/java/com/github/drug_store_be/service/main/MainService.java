package com.github.drug_store_be.service.main;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.web.DTO.MainPage.MainPageAdImg;
import com.github.drug_store_be.web.DTO.MainPage.MainPageProductResponse;
import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class MainService{
    private final ProductJpa productJpa;
    private final LikesJpa likesJpa;
    private final UserJpa userJpa;



    //정렬+광고
    public MainPageResponse mainpage(String sortBy,Pageable pageable) {
        //모든 상품 찾기
        List<Product> productList=productJpa.findAll();

        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);

        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);

        // Pageable로 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedMainPageProductResponseList.size());

        Page<MainPageProductResponse> paginatedResult;
        if (start > end) {
            // start 인덱스가 end 인덱스보다 큰 경우 빈 페이지를 반환
            paginatedResult =new PageImpl<>(Collections.emptyList(), pageable, sortedMainPageProductResponseList.size());
        }else {

            List<MainPageProductResponse> paginatedList = sortedMainPageProductResponseList.subList(start, end);
            paginatedResult = new PageImpl<>(paginatedList, pageable, sortedMainPageProductResponseList.size());
        }
        //광고 이미지
        productJpa.updateReviewAvg();
        Product topProductByReview = productJpa.findTopByOrderByReviewAvgDesc();
        productJpa.updateProductSales();
        Product topProductBySales = productJpa.findTopByOrderByProductSalesDesc();
        Integer productId= likesJpa.findProductWithMostLikes().getProductId();
        Optional<Product> topProductByLikes = productJpa.findById(productId);

        MainPageAdImg mpai=MainPageAdImg.builder()
                .likes_top_image_url(topProductByLikes.map(Product::getMainImgUrls).orElse(null))
                .sales_top_image_url(topProductBySales.getMainImgUrls(topProductBySales))
                .review_top_image_url(topProductByReview.getMainImgUrls(topProductByReview))
                .build();

        // MainPageResponse 생성
        MainPageResponse mainPageResponse = MainPageResponse.builder()
                .product_list(paginatedResult.getContent())
                .main_page_ad_img(mpai)
                .total_pages(paginatedResult.getTotalPages())
                .total_elements(paginatedResult.getTotalElements())
                .current_page(pageable.getPageNumber())
                .page_size(pageable.getPageSize())
                .build();

        return mainPageResponse;
    }

    //페이징+정렬
    public Page<MainPageProductResponse> CategoryPage(int category, String sortBy, Pageable pageable) {
        //카테고리별 상품 찾기
        List<Product> productList=productJpa.findByCategory(category);

        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);

        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedMainPageProductResponseList.size());

        if (start > end) {
            // start 인덱스가 end 인덱스보다 큰 경우 빈 페이지를 반환
            return new PageImpl<>(Collections.emptyList(), pageable, sortedMainPageProductResponseList.size());
        }

        List<MainPageProductResponse> pageContent = sortedMainPageProductResponseList.subList(start, end);
        return new PageImpl<>(pageContent, pageable, sortedMainPageProductResponseList.size());
    }

    //페이징+정렬+검색
    public Page<MainPageProductResponse> findPage(String keyword, String sortBy, Pageable pageable) {
        //브랜드와 상품 이름으로 검색
        List<Product> productList=productJpa.findByBrandOrProductNameContaining(keyword);

        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);

        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy.toString(),productListQueryDtoList);

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedMainPageProductResponseList.size());

        if (start > end) {
            // start 인덱스가 end 인덱스보다 큰 경우 빈 페이지를 반환
            return new PageImpl<>(Collections.emptyList(), pageable, sortedMainPageProductResponseList.size());
        }

        List<MainPageProductResponse> pageContent = sortedMainPageProductResponseList.subList(start, end);
        return new PageImpl<>(pageContent, pageable, sortedMainPageProductResponseList.size());
    }


    /**메소드 영역**/

    //user가 product를 like했는가 여부를 알기 위한 userId 찾기
    private Optional<Integer> getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Email = authentication.getName();
            Optional<Integer> userId = userJpa.findByEmail(Email);
            return userId;
    }

    //MainResponse에 필요한 값과 정렬에 필요한 값을 합쳐 놓은 dto
    public List<productListQueryDto> getProductListQueryDto(List<Product> productList) {
        Optional<Integer> userId = getUserId();
        List<productListQueryDto> plqdList = new ArrayList<>();

        for (Product product : productList) {
            Likes userLike;
            if(userId.isPresent()) {
                userLike= likesJpa.findByUserIdAndProductId(userId, product.getProductId());
            }else {userLike =null;}
            productJpa.updateProductSales();
            productJpa.updateReviewAvg();
            int productLike=likesJpa.findByProductId(product.getProductId());
            productListQueryDto plqd = productListQueryDto.builder()
                    .product_id(product.getProductId())
                    .product_name(product.getProductName())
                    .brand_name(product.getBrand())
                    .price(product.getPrice())
                    .final_price(product.getFinalPrice())
                    .product_img(product.getMainImgUrls(product))
                    .best(product.isBest())
                    .likes(userLike != null && userLike.getLikesId() != null)
                    .sales(product.getProductDiscount()>0)
                    .product_sales(product.getProductSales())
                    .product_like(productLike)
                    .review_avg(product.getReviewAvg())
                    .build();

            plqdList.add(plqd);
        }
        return plqdList;
    }



    //정렬
    List<MainPageProductResponse> pageSorting(String sortBy, List<productListQueryDto> productList) {

        Comparator<productListQueryDto> comparator;
        switch (sortBy) {
            case "likes":
                comparator = Comparator.comparing(productListQueryDto::getProduct_like, Comparator.reverseOrder());
                break;
            case "new":
                comparator = Comparator.comparing(productListQueryDto::getProduct_id, Comparator.reverseOrder());
                break;
            case "price":
                comparator = Comparator.comparing(productListQueryDto::getPrice, Comparator.naturalOrder());
                break;
            case "reviews":
                comparator = Comparator.comparing(productListQueryDto::getReview_avg, Comparator.reverseOrder());
                break;
            case "sales":
                comparator = Comparator.comparing(productListQueryDto::getProduct_sales, Comparator.reverseOrder());
                break;
            default:
                throw new IllegalArgumentException("Invalid sortBy parameter: " + sortBy);
        }

        return productList.stream()
                .sorted(comparator)
                .map(productListQueryDto::toMainpageResponseDto)
                .collect(Collectors.toList());
    }

}
