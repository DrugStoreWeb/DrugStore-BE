package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.detail.DetailService;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Detail.Answer;
import com.github.drug_store_be.web.DTO.Detail.ProductQAndAResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
@RequiredArgsConstructor
public class DetailController {
    private final DetailService detailService;
    private final LikesJpa likesJpa;

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
    @PostMapping("/answer")
    public ResponseDto adminAnswer(@RequestParam("question-id") Integer questionId, @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody Answer answer){
        return detailService.answerByAdminResult(questionId,customUserDetails,answer);

    }

    @GetMapping("/question/list")
    public ResponseDto getProductQAndA(@RequestParam("product-id")Integer productId){
        try {
            List<ProductQAndAResponse> productQAndAResponseList = detailService.productQuestionAndAnswer(productId);
            return new ResponseDto(HttpStatus.OK.value(),"조회 성공",productQAndAResponseList);
        } catch (NotFoundException e) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
    }
}
