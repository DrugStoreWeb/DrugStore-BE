package com.github.drug_store_be.service.main;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import com.github.drug_store_be.repository.review.ReviewJpa;
import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MainService {
    private final ProductJpa productJpa;
    private final ReviewJpa reviewJpa;
    private final LikesJpa likeJpa;

    Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();

    // Retrieve all products
    List<Product> productList = productJpa.findAll();


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
        return null;
    }

    //대표이미지 찾기
    public List<String> getMainImgUrls() {
        // Filter and map photo URLs
        List<String> photoUrls = productList.stream()
                .flatMap(product -> product.getProductPhotoList().stream()) // Flatten the list of ProductPhoto lists
                .filter(ProductPhoto::getPhotoType) // Filter by photo_type == true
                .map(ProductPhoto::getPhotoUrl) // Map to photo_url
                .collect(Collectors.toList()); // Collect to a list

        return photoUrls;
    }

    //사용자 좋아요 여부
//    public List<Boolean> getUserLike(){
//        List<Boolean> userlike = likesList.stream()
//    }
//

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
