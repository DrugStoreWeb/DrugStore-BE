package com.github.drug_store_be.service.like;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.AlreadyExistsException;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Like.MyLikesResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserJpa userJpa;
    private final ProductJpa productJpa;
    private final LikesJpa likesJpa;

    private User findUser(CustomUserDetails customUserDetails) {
        Integer userId = customUserDetails.getUserId();
        return userJpa.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));
    }

    private Product findProduct(Integer productId) {
        return productJpa.findById(productId)
                .orElseThrow(() -> new NotFoundException("제품을 찾을 수 없습니다."));
    }

        public List<MyLikesResponse> getMyLikes(CustomUserDetails customUserDetails) {
            User user = findUser(customUserDetails);
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

    public ResponseDto addMyLike(CustomUserDetails customUserDetails, Integer productId) {
        User user = findUser(customUserDetails);
        Product product = findProduct(productId);

        if(likesJpa.existsByUserAndProduct(user, product)){
            throw new AlreadyExistsException("이미 좋아요를 누른 제품입니다.");
        }

        Likes like = Likes.builder()
                .user(user)
                .product(product)
                .build();

        try{
            likesJpa.save(like);
        }catch (DataAccessException e){
            throw new RuntimeException("좋아요 추가 중 오류가 발생하였습니다.");
        }

        return new ResponseDto(HttpStatus.OK.value(),"좋아요 추가 성공");
    }

    @Transactional
    public ResponseDto deleteMyLike(CustomUserDetails customUserDetails, Integer productId) {
        User user = findUser(customUserDetails);
        Product product = findProduct(productId);

        Likes like = likesJpa.findByUserAndProduct(user, product)
                        .orElseThrow(() -> new NotFoundException("해당 제품에 대한 좋아요를 찾을 수 없습니다."));

        try{
            likesJpa.delete(like);
        }catch (DataAccessException e){
            throw new RuntimeException("좋아요 취소 중 오류가 발생하였습니다.");
        }

        return new ResponseDto(HttpStatus.OK.value(), "좋아요 취소 성공");
    }
}
