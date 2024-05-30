package com.github.drug_store_be.service.MyPage;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.cart.CartJpa;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsJpa;
import com.github.drug_store_be.repository.order.Orders;
import com.github.drug_store_be.repository.order.OrdersJpa;
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
import com.github.drug_store_be.service.exceptions.ReviewException;
import com.github.drug_store_be.web.DTO.Mypage.OrdersResponse;
import com.github.drug_store_be.web.DTO.Mypage.ReviewRequest;
import com.github.drug_store_be.web.DTO.Mypage.ReviewResponse;
import com.github.drug_store_be.web.DTO.Mypage.UserInfoResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final ReviewJpa reviewJpa;
    private final OrdersJpa ordersJpa;
    private final CartJpa cartJpa;
    private final OptionsJpa optionsJpa;
    private final UserJpa userJpa;
    private final ProductJpa productJpa;

    public ResponseDto addReview(CustomUserDetails customUserDetails, ReviewRequest reviewRequest, int ordersId) throws ReviewException {
        Integer userId = customUserDetails.getUserId();

        if (getReviewStatusForOrder(userId, ordersId)) {
            throw new ReviewException("이미 리뷰를 작성하였습니다.");
        }

        Orders orders = ordersJpa.findById(ordersId).orElseThrow(() -> new NotFoundException("order에서 주문을 찾을 수 없습니다."));
        Integer cartId = orders.getCart().getCartId();

        Cart cart = cartJpa.findById(cartId).orElseThrow(() -> new NotFoundException("cart에서 주문을 찾을 수 없습니다."));
        Integer optionId = cart.getOptions().getOptionsId();

        Options options = optionsJpa.findById(optionId).orElseThrow(() -> new NotFoundException("주문한 옵션을 찾을 수 없습니다."));
        Integer productId = options.getProduct().getProductId();

        Product product = productJpa.findById(productId).orElseThrow(() -> new NotFoundException("주문한 상품을 찾을 수 없습니다."));

        Integer reviewScore = reviewRequest.getReview_score();
        String reviewContent = reviewRequest.getReview_content();
        String photoUrl = getTruePhotoUrl(product.getProductPhotoList());

        if (reviewScore < 0 || reviewScore > 5) {
            throw new IllegalArgumentException("평점은 0부터 5까지 가능합니다.");
        }

        Review review = Review.builder()
                .user(User.builder().userId(userId).build())
                .orders(orders)
                .reviewScore(reviewScore)
                .product(product)
                .reviewContent(reviewContent)
                .createAt(LocalDate.now())
                .build();

        Review savedReview = reviewJpa.save(review);

        ReviewResponse response = ReviewResponse.builder()
                .reviewId(savedReview.getReviewId())
                .optionName(options.getOptionsName())
                .reviewScore(reviewScore)
                .reviewContent(reviewContent)
                .price(review.getOrders().getCart().getOptions().getProduct().getPrice())
                .brand(review.getOrders().getCart().getOptions().getProduct().getBrand())
                .ordersId(review.getOrders().getOrdersId())
                .productName(product.getProductName())
                .productImg(photoUrl)
                .createAt(savedReview.getCreateAt())
                .build();

        return new ResponseDto(HttpStatus.OK.value(), "리뷰가 저장되었습니다.", response);
    }

    public ResponseDto updateReview(CustomUserDetails customUserDetails, ReviewRequest reviewRequest, Integer ordersId) {
        Integer userId = customUserDetails.getUserId();
        Integer reviewScore = reviewRequest.getReview_score();
        String reviewContent = reviewRequest.getReview_content();

        Orders orders = ordersJpa.findById(ordersId).orElseThrow(() -> new NotFoundException("order에서 주문을 찾을 수 없습니다."));
        Integer cartId = orders.getCart().getCartId();

        Cart cart = cartJpa.findById(cartId).orElseThrow(() -> new NotFoundException("cart에서 주문을 찾을 수 없습니다."));
        Integer optionId = cart.getOptions().getOptionsId();

        Options options = optionsJpa.findById(optionId).orElseThrow(() -> new NotFoundException("주문한 옵션을 찾을 수 없습니다."));
        Integer productId = options.getProduct().getProductId();

        Product product = productJpa.findById(productId).orElseThrow(() -> new NotFoundException("주문한 상품을 찾을 수 없습니다."));

        String photoUrl = getTruePhotoUrl(product.getProductPhotoList());

        if (reviewScore < 0 || reviewScore > 5) {
            throw new IllegalArgumentException("평점은 0부터 5까지 가능합니다.");
        }

        Optional<Review> existingReview = reviewJpa.findByUserIdAndOrdersId(userId, ordersId);
        if (existingReview.isEmpty()) {
            throw new NotFoundException("리뷰를 찾을 수 없습니다.");
        }

        Review review = existingReview.get();
        review.setReviewScore(reviewScore);
        review.setReviewContent(reviewContent);
        review.setCreateAt(LocalDate.now());

        Review updateReview = reviewJpa.save(review);

        ReviewResponse response = ReviewResponse.builder()
                .reviewId(updateReview.getReviewId())
                .optionName(options.getOptionsName())
                .reviewScore(reviewScore)
                .reviewContent(reviewContent)
                .price(review.getOrders().getCart().getOptions().getProduct().getPrice())
                .brand(review.getOrders().getCart().getOptions().getProduct().getBrand())
                .ordersId(review.getOrders().getOrdersId())
                .productName(product.getProductName())
                .productImg(photoUrl)
                .createAt(updateReview.getCreateAt())
                .build();
        return new ResponseDto(HttpStatus.OK.value(), "리뷰가 수정되었습니다.", response);
    }

    public ResponseDto deleteReview(CustomUserDetails customUserDetails, Integer ordersId) {
        Integer userId = customUserDetails.getUserId();
        Optional<Review> existingReview = reviewJpa.findByUserIdAndOrdersId(userId, ordersId);

        if (existingReview.isPresent()) {
            Review review = existingReview.get();

            if (!review.getUser().getUserId().equals(customUserDetails.getUserId())) {
                throw new IllegalArgumentException("해당 리뷰를 삭제할 권한이 없습니다.");
            }

            reviewJpa.delete(review);

            return new ResponseDto(HttpStatus.OK.value(), "리뷰가 삭제되었습니다.");
        } else {
            throw new NotFoundException("해당 제품에 대한 리뷰를 찾을 수 없습니다.");
        }
    }

    public ResponseDto findUserDetail(CustomUserDetails customUserDetails) {
        User user = userJpa.findById(customUserDetails.getUserId()).orElseThrow(() -> new NotFoundException("회원가입 후 이용해 주시길 바랍니다."));

        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .name(user.getName())
                .nickName(user.getNickname())
                .birthday(user.getBirthday())
                .address(user.getAddress())
                .profilePic(user.getProfilePic())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();

        return new ResponseDto(HttpStatus.OK.value(), "조회 성공", userInfoResponse);
    }

    public ResponseDto findAllOrders(CustomUserDetails customUserDetails, Pageable pageable) {
        int userId = userJpa.findById(customUserDetails.getUserId()).map(User::getUserId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));

        Page<Orders> ordersPage = ordersJpa.findAllByUserId(userId, pageable);

        if (ordersPage.isEmpty()) {
            throw new NotFoundException("구매 정보를 찾을 수 없습니다.");
        }

        Page<OrdersResponse> responsePage = ordersPage.map(orders -> OrdersResponse.builder()
                .ordersId(orders.getOrdersId())
                .productImg(orders.getCart().getOptions().getProduct().getProductPhotoList().stream().map(ProductPhoto::getPhotoUrl).collect(Collectors.toList()))
                .price(orders.getCart().getOptions().getProduct().getPrice())
                .productName(orders.getCart().getOptions().getProduct().getProductName())
                .optionName(orders.getCart().getOptions().getOptionsName())
                .brand(orders.getCart().getOptions().getProduct().getBrand())
                .review_status(getReviewStatusForOrder(userId, orders.getOrdersId()))
                .review_deadline(getReviewDeadline(orders.getOrdersAt()))
                .build());

        return new ResponseDto(HttpStatus.OK.value(), "", responsePage);
    }

    public LocalDate getReviewDeadline(LocalDate orderAt) {
        return orderAt.plusDays(30); // 구매일로부터 30일 후를 리뷰 마감일로 설정
    }

    public Boolean getReviewStatusForOrder(Integer userId, Integer orderId) {
        Optional<Review> existingReview = reviewJpa.findByUserIdAndOrdersId(userId, orderId);

        return existingReview.isPresent(); // 리뷰가 존재하면 true, 없으면 false 반환
    }

    public ResponseDto findAllReviewsByOrdersId(int ordersId, Pageable pageable) {
        Page<Review> reviews = reviewJpa.findAllReviewsByOrdersId(ordersId, pageable);
        if (reviews.isEmpty()) {
            throw new NotFoundException("등록된 리뷰가 존재하지 않습니다.");
        }
        Orders orders = ordersJpa.findById(ordersId).orElseThrow(() -> new NotFoundException("order에서 주문을 찾을 수 없습니다."));
        Integer cartId = orders.getCart().getCartId();

        Cart cart = cartJpa.findById(cartId).orElseThrow(() -> new NotFoundException("cart에서 주문을 찾을 수 없습니다."));
        Integer optionId = cart.getOptions().getOptionsId();

        Options options = optionsJpa.findById(optionId).orElseThrow(() -> new NotFoundException("주문한 옵션을 찾을 수 없습니다."));
        Integer productId = options.getProduct().getProductId();

        Product product = productJpa.findById(productId).orElseThrow(() -> new NotFoundException("주문한 상품을 찾을 수 없습니다."));

        String photoUrl = getTruePhotoUrl(product.getProductPhotoList());

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .reviewId(review.getReviewId())
                        .reviewScore(review.getReviewScore())
                        .reviewContent(review.getReviewContent())
                        .productImg(photoUrl)
                        .ordersId(review.getOrders().getOrdersId())
                        .productName(review.getOrders().getCart().getOptions().getProduct().getProductName())
                        .price(review.getOrders().getCart().getOptions().getProduct().getPrice())
                        .optionName(review.getOrders().getCart().getOptions().getOptionsName())
                        .brand(review.getOrders().getCart().getOptions().getProduct().getBrand())
                        .createAt(review.getCreateAt())
                        .build())
                .collect(Collectors.toList());

        return new ResponseDto(HttpStatus.OK.value(), "", reviewResponses);
    }

    public String getTruePhotoUrl(List<ProductPhoto> productPhotoList) {
        Optional<String> optionalPhotoUrl = productPhotoList.stream()
                .filter(ProductPhoto::isPhotoType) // type이 true인 경우만 필터링
                .map(ProductPhoto::getPhotoUrl) // photoUrl만 추출
                .findFirst();

        return optionalPhotoUrl.orElse("");
    }
}

