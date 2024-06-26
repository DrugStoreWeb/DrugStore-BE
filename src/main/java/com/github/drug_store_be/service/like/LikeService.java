package com.github.drug_store_be.service.like;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesRepository;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductRepository;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.user.User;

import com.github.drug_store_be.repository.user.UserRepository;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Like.MyLikesResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final LikesRepository likesRepository;

    private User findUser(CustomUserDetails customUserDetails) {
        Integer userId = customUserDetails.getUserId();
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));
    }

    private Product findProduct(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("제품을 찾을 수 없습니다."));
    }

        public List<MyLikesResponse> getMyLikes(CustomUserDetails customUserDetails) {
            User user = findUser(customUserDetails);
            List<Likes> likes = likesRepository.findByUser(user);

            return likes.stream()
                    .map(like -> {
                        MyLikesResponse myLikesResponse = new MyLikesResponse();
                        myLikesResponse.setProductId(like.getProduct().getProductId());
                        myLikesResponse.setProductName(like.getProduct().getProductName());
                        myLikesResponse.setPrice(like.getProduct().getPrice());
                        myLikesResponse.setBrandName(like.getProduct().getBrand());
                        myLikesResponse.setFinalPrice(like.getProduct().getFinalPrice());

                        String productMainPhoto = like.getProduct().getProductPhotoList().stream()
                                .filter(ProductPhoto::isPhotoType)
                                .map(ProductPhoto::getPhotoUrl)
                                .findFirst()
                                .orElse("");
                        myLikesResponse.setProductImg(productMainPhoto);
                        myLikesResponse.setLikes(true);
                        return myLikesResponse;
                    })
                    .collect(Collectors.toList());
        }

    public ResponseDto addMyLike(CustomUserDetails customUserDetails, Integer productId) {
        User user = findUser(customUserDetails);
        Product product = findProduct(productId);

        Likes likes = Likes.builder()
                .user(user)
                .product(product)
                .build();

            likesRepository.save(likes);

        return new ResponseDto(HttpStatus.OK.value(),"좋아요 추가 성공");
    }

    @Transactional
    public ResponseDto deleteMyLike(CustomUserDetails customUserDetails, Integer productId) {
        User user = findUser(customUserDetails);
        Product product = findProduct(productId);

        likesRepository.deleteByUserAndProduct(user, product);

        return new ResponseDto(HttpStatus.OK.value(), "좋아요 취소 성공");
    }
}
