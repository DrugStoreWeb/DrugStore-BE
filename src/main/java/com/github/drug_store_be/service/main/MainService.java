package com.github.drug_store_be.service.main;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.option.OptionsJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import com.github.drug_store_be.repository.review.ReviewJpa;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.web.DTO.MainPage.MainPageAdImg;
import com.github.drug_store_be.web.DTO.MainPage.MainPageProductResponse;
import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MainService{
    private final ProductJpa productJpa;
    private final ProductPhotoJpa productPhotoJpa;
    private final LikesJpa likesJpa;
    private final UserJpa userJpa;
    private final ReviewJpa reviewJpa;
    private final OptionsJpa optionsJpa;


    //정렬+광고+페이징
    public ResponseDto mainpage(String sortBy, Pageable pageable) {
        //모든 상품 찾기
        List<Product> productList=productJpa.findAll();

        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);

        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);

        //mainpageAdImg
        List<MainPageAdImg> mainPageAdImgList = new ArrayList<>();

        // 리뷰가 가장 많은 제품
        Product topProductByReview = productJpa.findTopByOrderByReviewCountDesc();
        String reviewTopImageUrl = topProductByReview.getMainImgUrls(topProductByReview);
        mainPageAdImgList.add(new MainPageAdImg(reviewTopImageUrl));

        // 판매가 가장 많은 제품
        Product topProductBySales = productJpa.findTopByOrderBySalesDesc();
        String salesTopImageUrl = topProductBySales.getMainImgUrls(topProductBySales);
        mainPageAdImgList.add(new MainPageAdImg(salesTopImageUrl));

        // 좋아요가 가장 많은 제품
        Product topProductByLikes = productJpa.findTopByOrderByLikesDesc();
        String likesTopImageUrl = topProductByLikes.getMainImgUrls(topProductByLikes);
        mainPageAdImgList.add(new MainPageAdImg(likesTopImageUrl));
//
//// sortedMainPageProductResponseList와 mainPageAdImgList를 합치는 로직
//        List<MainPageResponse> mainPageResponseList = new ArrayList<>();
//
//// sortedMainPageProductResponseList를 추가
//        mainPageResponseList.addAll(sortedMainPageProductResponseList);
//
//// mainPageAdImgList를 MainPageResponse로 변환하여 추가
//        for (MainPageAdImg adImg : mainPageAdImgList) {
//            MainPageResponse adResponse = new MainPageResponse(adImg.getUrl());
//            mainPageResponseList.add(adResponse);
//        }
//
//// 페이지로 만들기
//        int pageSize = pageable.getPageSize();
//        int page = pageable.getPageNumber();
//        int start = page * pageSize;
//        int end = Math.min(start + pageSize, mainPageResponseList.size());
//        List<MainPageResponse> pageContent = mainPageResponseList.subList(start, end);
//        Page<MainPageResponse> pageResponse = new PageImpl<>(pageContent, pageable, mainPageResponseList.size());
//
//        return new ResponseDto(pageResponse);
    }

//    //페이징+정렬
//    public ResponseDto CategoryPage(String category, String sortBy, Pageable pageable) {
//        //카테고리별 상품 찾기
//        List<Product> productList=productJpa.findByCategory(category);
//        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
//        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);
//        //sorting
//        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);
//        return null;
//    }
//
//
//    //페이징+정렬+검색
//    public ResponseDto findPage(String keyword, String pageable, Pageable sortBy) {
//        //브랜드와 상품 이름으로 검색
//        List<Product> productList=productJpa.findByBrandOrProductName(keyword);
//        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
//        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);
//        //sorting
//        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy.toString(),productListQueryDtoList);
//        return null;
//    }


    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userEmail = user.getUsername();
        Integer userId = userJpa.findByEmail(userEmail);
        if (userId == null) {
            throw new IllegalStateException("User not found: " + userEmail);
        }
        return userId;
    }

    public List<productListQueryDto> getProductListQueryDto(List<Product> productList) {
        Integer userId = getUserId();
        List<productListQueryDto> plqdList = new ArrayList<>();

        for (Product product : productList) {
            Likes userLike = (Likes) likesJpa.findByUserIdAndProductId(userId, product.getProductId());
            productJpa.updateProductSales(product.getProductId(), optionsJpa.getTotalOptionsStock());
            productJpa.updateReviewAvg();

            productListQueryDto plqd = productListQueryDto.builder()
                    .product_id(product.getProductId())
                    .product_name(product.getProductName())
                    .brand_name(product.getBrand())
                    .price(product.getPrice())
                    .final_price(product.getFinalPrice())
                    .product_img(product.getMainImgUrls(product))
                    .best(product.isBest())
                    .likes(userLike != null && userLike.getLikesId() != null)
                    .sales(product.getProductSales() != null)
                    .product_sales(product.getProductSales())
                    .product_like(likesJpa.findByProductId(product.getProductId()))
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
                comparator = Comparator.comparing(productListQueryDto::getPrice, Comparator.reverseOrder());
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
