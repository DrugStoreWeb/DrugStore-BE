package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.detail.DetailService;
import com.github.drug_store_be.web.DTO.Detail.ProductDetailResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product")
@RequiredArgsConstructor
public class DetailController {
    private final DetailService detailService;

    @GetMapping(value = "/detail")
    public ResponseDto productDetail(@RequestParam("product-id") Integer productId , @AuthenticationPrincipal CustomUserDetails customUserDetails){

        if (customUserDetails!=null){
            return detailService.productDetailResult(productId,customUserDetails);
        }else {
            return detailService.productDetailResultByNotLogin(productId);
        }

    }
    @GetMapping("/review/{productId}")
    public ResponseDto productReviews(
            @RequestParam(required = false,defaultValue = "0",value ="page")Integer pageNum,
            @RequestParam(required = false,defaultValue = "createAt",value ="sort")String criteria,
            @PathVariable Integer productId ){
        return detailService.productReviewResult(productId,pageNum,criteria);
    }
}
