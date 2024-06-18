package com.github.drug_store_be.service.main;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductRepository;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserRepository;

import com.github.drug_store_be.web.DTO.MainPage.MainPageAdImg;
import com.github.drug_store_be.web.DTO.MainPage.MainPageProductResponse;
import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class MainService{
    private final ProductRepository productRepository;
    private final UserRepository userRepository;



    //정렬+광고
    public MainPageResponse mainpage(String sortBy,Pageable pageable) {
        //모든 상품 찾기
        List<Product> productList= productRepository.findAll();

        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);

        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);

        // Pageable로 페이징 처리
        Page<MainPageProductResponse> paginatedResult =getPaging(sortedMainPageProductResponseList, pageable);

        //광고 이미지
        MainPageAdImg mpai=getAdImg();

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
        List<Product> productList=productRepository.findByCategoryCategoryId(category);

        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);

        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);

        // Pageing
        Page<MainPageProductResponse> paginatedResult =getPaging(sortedMainPageProductResponseList, pageable);

        return paginatedResult;
    }

    //페이징+정렬+검색
    public Page<MainPageProductResponse> findPage(String keyword, String sortBy, Pageable pageable) {
        //브랜드와 상품 이름으로 검색
        List<Product> productList=productRepository.findByKeyword(keyword);

        //product+productPhoto+sorting기준 필드=productListQueryDto 생성
        List<productListQueryDto> productListQueryDtoList=getProductListQueryDto(productList);

        //sorting
        List<MainPageProductResponse> sortedMainPageProductResponseList=pageSorting(sortBy,productListQueryDtoList);

        // Pageing
        Page<MainPageProductResponse> paginatedResult =getPaging(sortedMainPageProductResponseList, pageable);

        return paginatedResult;
    }


    /**메소드 영역**/


    //대표이미지 찾기
    public String getMainImgUrls(Product product) {
        if (product == null || product.getProductPhotoList() == null) {
            return ""; // Product가 null이거나 사진 목록이 없는 경우 빈 문자열 반환
        }
        return product.getProductPhotoList().stream()
                .filter(ProductPhoto::isPhotoType) // photoType이 true인 경우 필터링
                .map(ProductPhoto::getPhotoUrl) // photoUrl로 매핑
                .findFirst() // 첫 번째 요소를 찾음
                .orElse(""); // 값이 없으면 빈 문자열 반환
    }


    //광고이미지
    public MainPageAdImg getAdImg() {

        productRepository.updateReviewAvg();
        productRepository.updateProductSales();

        Product topProductByReview = productRepository.findTopByOrderByReviewAvgDesc();
        Product topProductBySales = productRepository.findTopByOrderByProductSalesDesc();
        Product topProductByLikes = productRepository.findTopByOrderByLikesDesc();


        MainPageAdImg mpai = MainPageAdImg.builder()
                .likes_top_image_url(getMainImgUrls(topProductByLikes))
                .sales_top_image_url(getMainImgUrls(topProductBySales))
                .review_top_image_url(getMainImgUrls(topProductByReview))
                .build();
        return mpai;
    }


    //페이징
    public Page<MainPageProductResponse> getPaging(List<MainPageProductResponse> p, Pageable pageable){
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), p.size());

        Page<MainPageProductResponse> paginatedResult;
        if (start > end) {
            // start 인덱스가 end 인덱스보다 큰 경우 빈 페이지를 반환
            paginatedResult =new PageImpl<>(Collections.emptyList(), pageable, p.size());
        }else {
            List<MainPageProductResponse> paginatedList = p.subList(start, end);
            paginatedResult = new PageImpl<>(paginatedList, pageable, p.size());
        }
        return paginatedResult;
    }


    //user가 product를 like했는가 여부를 알기 위한 userId 찾기
    private Boolean getUserLike(Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmailFetchJoin(email).orElse(null);

        if (user != null) {
            Integer userId = user.getUserId();  // userId 필드를 직접 추출
            return productRepository.existsByUserIdAndProductId(product.getProductId(), userId);
        } else {
            return false;
        }
    }


    //MainResponse에 필요한 값과 정렬에 필요한 값을 합쳐 놓은 dto
    public List<productListQueryDto> getProductListQueryDto(List<Product> productList) {
        List<productListQueryDto> plqdList = new ArrayList<>();

        for (Product product : productList) {

            productRepository.updateProductSales();
            productRepository.updateReviewAvg();
            int productLike=productRepository.countLikesByProductId(product.getProductId());
            productListQueryDto plqd = productListQueryDto.builder()
                    .product_id(product.getProductId())
                    .product_name(product.getProductName())
                    .brand_name(product.getBrand())
                    .price(product.getPrice())
                    .final_price(product.getFinalPrice())
                    .product_img(getMainImgUrls(product))
                    .best(product.isBest())
                    .likes(getUserLike(product))
                    .sales(product.getProductDiscount()>0)
                    .product_sales(product.getProductSales())
                    .product_like(productLike)
                    .review_avg(product.getReviewAvg())
                    .build();

            plqdList.add(plqd);
        }
        return plqdList;
    }

    public static MainPageProductResponse toMainpageResponseDto(productListQueryDto pld) {
        return MainPageProductResponse.builder()
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
                .map(MainService::toMainpageResponseDto)
                .collect(Collectors.toList());
    }

}