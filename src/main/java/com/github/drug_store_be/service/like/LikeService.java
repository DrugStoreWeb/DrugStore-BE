package com.github.drug_store_be.service.like;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserJpa userJpa;
    private final ProductJpa productJpa;
    private final LikesJpa likesJpa;

    public ResponseDto addProductLike(CustomUserDetails userDetails, Integer productId) {
        Integer userId = userDetails.getUserId();
        User user = userJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));
        Product product = productJpa.findById(productId)
                .orElseThrow(() -> new NotFoundException("제품을 찾을 수 없습니다."));

        Likes like = Likes.builder()
                .user(user)
                .product(product)
                .build();

        likesJpa.save(like);
        return new ResponseDto(HttpStatus.OK.value(),"좋아요 추가 성공");
    }
}
