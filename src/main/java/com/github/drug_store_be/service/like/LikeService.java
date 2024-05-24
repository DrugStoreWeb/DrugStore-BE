package com.github.drug_store_be.service.like;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Like.MyLikesResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserJpa userJpa;
    private final ProductJpa productJpa;
    private final LikesJpa likesJpa;

    private User findUser(Integer userId) {
        return userJpa.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));
    }

        public List<MyLikesResponse> getMyLikes(CustomUserDetails customUserDetails) {
            Integer userId = customUserDetails.getUserId();
            User user = findUser(userId);
            List<Likes> likes = likesJpa.findByUser(user);

            return likes.stream()
                    .map(like -> {
                        MyLikesResponse myLikesResponse = new MyLikesResponse();
                        myLikesResponse.setProductId(like.getProduct().getProductId());
                        myLikesResponse.setProductName(like.getProduct().getProductName());
                        myLikesResponse.setPrice(like.getProduct().getPrice());
                        myLikesResponse.setBrandName(like.getProduct().getBrand());

                        String productMainPhoto = like.getProduct().getProductPhotoList().stream()
                                .filter(ProductPhoto::isPhotoType)
                                .map(ProductPhoto::getPhotoUrl)
                                .findFirst()
                                .orElse("");
                        myLikesResponse.setProductImag(productMainPhoto);
                        return myLikesResponse;
                    })
                    .collect(Collectors.toList());
        }

    public ResponseDto addProductLike(CustomUserDetails customUserDetails, Integer productId) {
        Integer userId = customUserDetails.getUserId();
        User user = findUser(userId);
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
