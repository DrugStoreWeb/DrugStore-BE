package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.MyPage.MypageService;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.service.exceptions.ReviewException;
import com.github.drug_store_be.web.DTO.Mypage.ReviewRequest;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/mypage")
public class MypageController {
    private final MypageService mypageService;

    @PostMapping("/review/{ordersId}")
    public ResponseDto AddReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ReviewRequest reviewRequest, @PathVariable("ordersId") Integer ordersId) {
        try {
            return mypageService.addReview(customUserDetails, reviewRequest, ordersId);
        } catch (ReviewException e) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }
    @PutMapping("/review/{ordersId}")
    public ResponseDto UpdateReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ReviewRequest reviewRequest, @PathVariable("ordersId") Integer ordersId) {
        return mypageService.updateReview(customUserDetails, reviewRequest, ordersId);
    }
    @DeleteMapping("/review/{ordersId}")
    public ResponseDto deleteReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("ordersId") Integer ordersId) {
        try {
            return mypageService.deleteReview(customUserDetails, ordersId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/userInfo")
    public ResponseDto MyPageUserDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return mypageService.findUserDetail(customUserDetails);
    }

    @GetMapping("/order")
    public ResponseDto MyPageOrderList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        return mypageService.findAllOrders(customUserDetails, pageable);
    }

    @GetMapping("/review/{ordersId}")
    public ResponseDto getReviews(@PathVariable("ordersId") int ordersId, Pageable pageable) {
        return mypageService.findAllReviewsByOrdersId(ordersId, pageable);
    }

    @GetMapping("/myCoupon")
    public ResponseDto myCoupon(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return mypageService.findAllCoupon(customUserDetails);
    }

    @GetMapping("/questionAnswer")
    public ResponseDto ReviewList(@AuthenticationPrincipal CustomUserDetails customUserDetails,Pageable pageable) {
        return mypageService.findAllQnA(customUserDetails,pageable);
    }
}
