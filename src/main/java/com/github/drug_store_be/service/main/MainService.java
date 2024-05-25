package com.github.drug_store_be.service.main;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.review.ReviewJpa;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MainService {
    private final ProductJpa productJpa;
    private final UserJpa userJpa;
    private final ReviewJpa reviewJpa;
    private final LikesJpa likeJpa;

    //현재 로그인한 유저 아이디
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User)authentication.getPrincipal();
    String userEmail=user.getUsername();
    int userId=userJpa.findByEmailFetchJoin(userEmail);


    //정렬+광고
    public ResponseDto mainpage(String sortBy, Pageable pageable) {
        return null;
    }

    //페이징+정렬
    public ResponseDto CategoryPage(String category, Pageable pageable) {
        return null;
    }


    //페이징+정렬+검색
    public ResponseDto findPage(String keyword, Pageable pageable) {
        Product products= (Product) productJpa.findByBrandOrProductName(keyword); //상품검색

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());
        return new PageImpl<>(result.subList(start, end), pageable, result.size());
        return null;
    }



    void getLists(Pageable pageable,Product products) {

    Likes userLike= (Likes) likeJpa.findByUserIdAndProductId(userId,products.getProductId());//상품에 유저가 좋아요를 눌렀는지 여부
    int productLike=likeJpa.findByProductId(products.getProductId());
    boolean isUserLike = (userLike.getLikesId() != null) ? true : false;
    boolean isSales=(products.getProductSales()!=null)? true:false; //세일 여부


    productListQueryDto plqd=productListQueryDto.builder()
            .product_id(products.getProductId())
            .product_name(products.getProductName())
            .brand_name(products.getBrand())
            .price(products.getPrice())
            .final_price(products.getFinalPrice())
            .product_img(products.getMainImgUrls(products))
            .likes(isUserLike)
            .best(products.isBest())
            .sales(isSales) //update product_sales
            .review_avg(products.getReviewAvg()) //update ReviewAvg
            .product_like(productLike)
            .build();

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
