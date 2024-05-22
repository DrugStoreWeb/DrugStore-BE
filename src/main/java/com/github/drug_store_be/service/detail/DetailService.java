package com.github.drug_store_be.service.detail;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import com.github.drug_store_be.repository.review.Review;
import com.github.drug_store_be.repository.review.ReviewJpa;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Detail.ProductDetailResponse;
import com.github.drug_store_be.web.DTO.Detail.ProductImg;
import com.github.drug_store_be.web.DTO.Detail.ProductOption;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetailService {
    private final ProductJpa productJpa;
    private final ReviewJpa reviewJpa;
    private final ProductPhotoJpa productPhotoJpa;
    private final OptionsJpa optionsJpa;
    private final UserJpa userJpa;
    private final LikesJpa likesJpa;
    public ResponseDto productDetailResult(Integer productId, CustomUserDetails customUserDetails) {
        User user =userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("토큰에 해당하는 유저를 찾을 수 없습니다."));
        Product product = productJpa.findById(productId)
                .orElseThrow(()-> new NotFoundException(productId+"에 해당하는 상세 페이지를 찾을 수 없습니다."));
        List<Review> reviewListByProduct =reviewJpa.findAllByProduct(product);
        List<Integer> reviewScoreList = reviewListByProduct.stream().map(Review::getReviewScore).collect(Collectors.toList());
//        int reviewScoreSum = reviewScoreList.stream().mapToInt((i)->i).sum();
        int reviewCount= reviewListByProduct.size();
//        Double reviewAvg = reviewScoreSum / (double)reviewCount;
        List<ProductPhoto> productPhotosByProduct=productPhotoJpa.findAllByProduct(product);
        List<Options> optionsByProduct = optionsJpa.findAllByProduct(product);
        List<ProductImg> productImgs = productPhotosByProduct.stream().map(ProductImg::new).toList();
        List<ProductOption> productOptions = optionsByProduct.stream().map(ProductOption::new).toList();
        ProductDetailResponse productDetailResponse= ProductDetailResponse.builder()
                .productId(productId)
                .productName(product.getProductName())
                .sales(product.getProductDiscount())
                .price(product.getPrice())
                .finalPrice(product.getFinalPrice())
                .productImg(productImgs)
                .reviewCount(reviewCount)
                .reviewAvg(product.getReviewAvg())
                .best(product.isBest())
                .brandName(product.getBrand())
                .productOptions(productOptions)
                .build();

            if (likesJpa.existsByUserAndProduct(user,product)) {
                productDetailResponse.setIsLike(true);
                log.error(likesJpa.existsByUserAndProduct(user,product)+"는");
                return new ResponseDto(HttpStatus.OK.value(), "조회 성공",productDetailResponse);
            }else {
                productDetailResponse.setIsLike(false);
                return new ResponseDto(HttpStatus.OK.value(), "조회 성공",productDetailResponse);
            }

    }

    public ResponseDto productDetailResultByNotLogin(Integer productId) {
        Product product = productJpa.findById(productId)
                .orElseThrow(()-> new NotFoundException(productId+"에 해당하는 상세 페이지를 찾을 수 없습니다."));
        List<Review> reviewListByProduct =reviewJpa.findAllByProduct(product);
        List<Integer> reviewScoreList = reviewListByProduct.stream().map(Review::getReviewScore).collect(Collectors.toList());
//        int reviewScoreSum = reviewScoreList.stream().mapToInt((i)->i).sum();
        int reviewCount= reviewListByProduct.size();
//        Double reviewAvg = reviewScoreSum / (double)reviewCount;
        List<ProductPhoto> productPhotosByProduct=productPhotoJpa.findAllByProduct(product);
        List<Options> optionsByProduct = optionsJpa.findAllByProduct(product);
        List<ProductImg> productImgs = productPhotosByProduct.stream().map(ProductImg::new).toList();
        List<ProductOption> productOptions = optionsByProduct.stream().map(ProductOption::new).toList();
        ProductDetailResponse productDetailResponse=  ProductDetailResponse.builder()
                .productId(productId)
                .productName(product.getProductName())
                .sales(product.getProductDiscount())
                .price(product.getPrice())
                .finalPrice(product.getFinalPrice())
                .productImg(productImgs)
                .reviewCount(reviewCount)
                .reviewAvg(product.getReviewAvg())
                .isLike(false)
                .best(product.isBest())
                .brandName(product.getBrand())
                .productOptions(productOptions)
                .build();
        return new ResponseDto(HttpStatus.OK.value(), "조회 성공",productDetailResponse);
    }
}
