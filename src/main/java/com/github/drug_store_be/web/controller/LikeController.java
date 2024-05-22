package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.like.LikeService;
import com.github.drug_store_be.web.DTO.Like.LikeRequest;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseDto likeAdd(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                               @RequestBody LikeRequest likeRequest) {
        return likeService.addProductLike(customUserDetails, likeRequest.getProductId());
    }
}
