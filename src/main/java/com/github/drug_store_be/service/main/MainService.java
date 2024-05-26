package com.github.drug_store_be.service.main;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.option.OptionsJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import com.github.drug_store_be.repository.review.ReviewJpa;
import com.github.drug_store_be.repository.user.UserJpa;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private String category;
    private String sortBy;
    private Pageable pageable;


    //정렬+광고
    public MainPageResponse mainpage(String sortBy) {
        //모든 상품 찾기
        List<Product> productList=productJpa.findAll();

        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);

        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);

        //광고 이미지
        productJpa.updateReviewAvg();
        Product topProductByReview = productJpa.findTopByOrderByReviewAvgDesc();
        productJpa.updateProductSales();
        Product topProductBySales = productJpa.findTopByOrderByProductSalesDesc();
        Integer productId= likesJpa.findProductWithMostLikes().getProductId();
        Optional<Product> topProductByLikes = productJpa.findById(productId);

        MainPageAdImg mpai=MainPageAdImg.builder()
                .likesTopImageUrl(topProductByLikes.map(Product::getMainImgUrls).orElse(null))
                .salesTopImageUrl(topProductBySales.getMainImgUrls(topProductBySales))
                .reviewTopImageUrl(topProductByReview.getMainImgUrls(topProductByReview))
                .build();

        //mainPageResponse
        MainPageResponse mainPageResponse=MainPageResponse.builder()
                .productList(sortedMainPageProductResponseList)
                .mainPageAdImg(mpai)
                .build();

        return mainPageResponse;
    }

    //페이징+정렬
    public Page<MainPageProductResponse> CategoryPage(int category, String sortBy, org.springframework.data.domain.Pageable pageable) {
        //카테고리별 상품 찾기
        List<Product> productList=productJpa.findByCategory(category);
        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);
        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);

        //List->page
        PageRequest pageRequest = PageRequest.of(0, 24);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), sortedMainPageProductResponseList.size());
        List<MainPageProductResponse> pageContent = sortedMainPageProductResponseList.subList(start, end);

        Page<MainPageProductResponse> page = new PageImpl<>(pageContent, pageRequest, sortedMainPageProductResponseList.size());
        return page;
    }


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
        Object principal = authentication.getPrincipal(); // Object 타입으로 받음

        // principal이 String 타입인 경우 처리
        if (principal instanceof String) {
            String userEmail = (String) principal; // 캐스팅
            Integer userId = userJpa.findByEmail(userEmail);
            return userId;
        } else {
            // principal이 User 타입인 경우 처리
            User user = (User) principal; // 캐스팅
            String userEmail = user.getUsername();
            Integer userId = userJpa.findByEmail(userEmail);
            return userId;
        }
    }

    public List<productListQueryDto> getProductListQueryDto(List<Product> productList) {
        Integer userId = getUserId();
        List<productListQueryDto> plqdList = new ArrayList<>();

        for (Product product : productList) {
            Likes userLike;
            if(userId!=null) {
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
                    .sales(product.getProductSales() != null)
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
