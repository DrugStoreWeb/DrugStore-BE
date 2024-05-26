package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.like.LikeService;
import com.github.drug_store_be.web.DTO.Like.LikeRequest;
import com.github.drug_store_be.web.DTO.Like.MyLikesResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/myList")
    public ResponseDto getLike(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MyLikesResponse> myLikesResponseList = likeService.getMyLikes(customUserDetails);
        return new ResponseDto(HttpStatus.OK.value(),"조회 성공", myLikesResponseList );
    }

    @PostMapping
    public ResponseDto addLike(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                               @RequestBody LikeRequest likeRequest) {
        return likeService.addMyLike(customUserDetails, likeRequest.getProductId());
    }

    @DeleteMapping("/delete")
    public ResponseDto deleteLike(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @RequestBody LikeRequest likeRequest) {
        return likeService.deleteMyLike(customUserDetails, likeRequest.getProductId());
    }
}
