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
import com.github.drug_store_be.web.DTO.MainPage.MainPageProductResponse;
import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
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

    //현재 로그인한 유저 아이디
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User)authentication.getPrincipal();
    String userEmail=user.getUsername();
    int userId= userJpa.findByEmail(userEmail);


    //정렬+광고
    public ResponseDto mainpage(String sortBy, Pageable pageable) {

        return null;
    }

    //페이징+정렬
    public ResponseDto CategoryPage(String category, String sortBy, Pageable pageable) {
        return null;
    }


    //페이징+정렬+검색
    public ResponseDto findPage(String keyword, String pageable, Pageable sortBy) {
        return null;
    }

    public List<productListQueryDto> getProductListQueryDto() {
        List<Product> productList = productJpa.findAll();
        List<productListQueryDto> plqdList = new ArrayList<>();

        for (Product product : productList) {
            Likes userLike = (Likes) likesJpa.findByUserIdAndProductId(userId, product.getProductId());
            productJpa.updateProductSales(product.getProductId(), product.getOriginalStock(), optionsJpa.findTotalOptionsStock());
            reviewJpa.updateReviewAvg();

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
