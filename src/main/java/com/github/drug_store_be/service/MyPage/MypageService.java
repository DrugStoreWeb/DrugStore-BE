package com.github.drug_store_be.service.MyPage;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsRepository;
import com.github.drug_store_be.repository.order.Orders;
import com.github.drug_store_be.repository.order.OrdersRepository;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductRepository;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.questionAnswer.QuestionAnswer;
import com.github.drug_store_be.repository.review.Review;
import com.github.drug_store_be.repository.review.ReviewRepository;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserRepository;
import com.github.drug_store_be.repository.userCoupon.UserCoupon;
import com.github.drug_store_be.repository.userCoupon.UserCouponRepository;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.service.exceptions.ReviewException;
import com.github.drug_store_be.web.DTO.Mypage.*;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.order.OrderCouponResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final ReviewRepository reviewRepository;
    private final OrdersRepository ordersRepository;
    private final OptionsRepository optionsRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserCouponRepository userCouponRepository;

    public ResponseDto addReview(CustomUserDetails customUserDetails, ReviewRequest reviewRequest, int ordersId) throws ReviewException {
        Integer userId = customUserDetails.getUserId();

        if (getReviewStatusForOrder(userId, ordersId)) {
            throw new ReviewException("이미 리뷰를 작성하였습니다.");
        }

        if (isCartExists(userId)) {
            throw new ReviewException("결제를 완료해야 리뷰를 작성할 수 있습니다.");
        }

        Integer reviewScore = reviewRequest.getReviewScore();
        String reviewContent = reviewRequest.getReviewContent();

        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new NotFoundException("order에서 주문을 찾을 수 없습니다."));

        Options options = optionsRepository.findById(orders.getOptions().getOptionsId())
                .orElseThrow(() -> new NotFoundException("해당 options를 찾을 수 없습니다."));

        Product product = productRepository.findById(options.getProduct().getProductId())
                .orElseThrow(() -> new NotFoundException("주문한 상품을 찾을 수 없습니다."));

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

        Review savedReview = reviewRepository.save(review);

        ReviewResponse response = ReviewResponse.builder()
                .reviewId(savedReview.getReviewId())
                .optionName(review.getOrders().getOptions().getOptionsName())
                .reviewScore(reviewScore)
                .reviewContent(reviewContent)
                .price(review.getOrders().getOptions().getProduct().getPrice())
                .brand(review.getOrders().getOptions().getProduct().getBrand())
                .ordersId(review.getOrders().getOrdersId())
                .productName(product.getProductName())
                .productImg(photoUrl)
                .createAt(savedReview.getCreateAt())
                .build();

        return new ResponseDto(HttpStatus.OK.value(), "리뷰가 저장되었습니다.", response);
    }

    public ResponseDto updateReview(CustomUserDetails customUserDetails, ReviewRequest reviewRequest, Integer ordersId) {
        Integer userId = customUserDetails.getUserId();
        Integer reviewScore = reviewRequest.getReviewScore();
        String reviewContent = reviewRequest.getReviewContent();

        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new NotFoundException("order에서 주문을 찾을 수 없습니다."));

        Options options = optionsRepository.findById(orders.getOptions().getOptionsId())
                .orElseThrow(() -> new NotFoundException("해당 options를 찾을 수 없습니다."));

        Product product = productRepository.findById(options.getProduct().getProductId())
                .orElseThrow(() -> new NotFoundException("주문한 상품을 찾을 수 없습니다."));
        String photoUrl = getTruePhotoUrl(product.getProductPhotoList());

        if (reviewScore < 0 || reviewScore > 5) {
            throw new IllegalArgumentException("평점은 0부터 5까지 가능합니다.");
        }

        Optional<Review> existingReview = reviewRepository.findByUserIdAndOrdersId(userId, ordersId);
        if (existingReview.isEmpty()) {
            throw new NotFoundException("리뷰를 찾을 수 없습니다.");
        }

        Review review = existingReview.get();
        review.setReviewScore(reviewScore);
        review.setReviewContent(reviewContent);
        review.setCreateAt(LocalDate.now());

        Review updateReview = reviewRepository.save(review);

        ReviewResponse response = ReviewResponse.builder()
                .reviewId(updateReview.getReviewId())
                .optionName(review.getOrders().getOptions().getOptionsName())
                .reviewScore(reviewScore)
                .reviewContent(reviewContent)
                .price(review.getOrders().getOptions().getProduct().getPrice())
                .brand(review.getOrders().getOptions().getProduct().getBrand())
                .ordersId(review.getOrders().getOrdersId())
                .productName(product.getProductName())
                .productImg(photoUrl)
                .createAt(updateReview.getCreateAt())
                .build();
        return new ResponseDto(HttpStatus.OK.value(), "리뷰가 수정되었습니다.", response);
    }

    public ResponseDto deleteReview(CustomUserDetails customUserDetails, Integer ordersId) {
        Integer userId = customUserDetails.getUserId();
        Optional<Review> existingReview = reviewRepository.findByUserIdAndOrdersId(userId, ordersId);

        if (existingReview.isPresent()) {
            Review review = existingReview.get();

            if (!review.getUser().getUserId().equals(customUserDetails.getUserId())) {
                throw new IllegalArgumentException("해당 리뷰를 삭제할 권한이 없습니다.");
            }

            reviewRepository.delete(review);

            return new ResponseDto(HttpStatus.OK.value(), "리뷰가 삭제되었습니다.");
        } else {
            throw new NotFoundException("해당 제품에 대한 리뷰를 찾을 수 없습니다.");
        }
    }

    public ResponseDto findUserDetail(CustomUserDetails customUserDetails) {
        User user = userRepository.findById(customUserDetails.getUserId()).orElseThrow(() -> new NotFoundException("회원가입 후 이용해 주시길 바랍니다."));

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
        int userId = userRepository.findById(customUserDetails.getUserId())
                .map(User::getUserId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));

        Page<Orders> ordersPage = ordersRepository.findAllByUserId(userId, pageable);

        if (ordersPage.isEmpty()) {
            throw new NotFoundException("구매 정보를 찾을 수 없습니다.");
        }

        List<OrdersResponse> ordersResponseList = ordersPage.stream().map(orders -> {
            Integer optionId = orders.getOptions().getOptionsId();
            Options options = optionsRepository.findById(optionId).orElseThrow(() -> new NotFoundException("주문한 옵션을 찾을 수 없습니다."));
            Integer productId = options.getProduct().getProductId();
            Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("주문한 상품을 찾을 수 없습니다."));
            String photoUrl = getTruePhotoUrl(product.getProductPhotoList());

            return OrdersResponse.builder()
                    .ordersId(orders.getOrdersId())
                    .productImg(photoUrl)
                    .price(product.getPrice())
                    .productName(product.getProductName())
                    .optionName(options.getOptionsName())
                    .brand(product.getBrand())
                    .reviewStatus(getReviewStatusForOrder(userId, orders.getOrdersId()))
                    .reviewDeadline(getReviewDeadline(orders.getOrdersAt()))
                    .build();
        }).collect(Collectors.toList());

        Page<OrdersResponse> responsePage = new PageImpl<>(ordersResponseList, pageable, ordersPage.getTotalElements());

        return new ResponseDto(HttpStatus.OK.value(), "구매 정보를 성공적으로 조회했습니다.", responsePage);
    }
    public LocalDate getReviewDeadline(LocalDate orderAt) {
        return orderAt.plusDays(30); // 구매일로부터 30일 후를 리뷰 마감일로 설정
    }

    public Boolean getReviewStatusForOrder(Integer userId, Integer ordersId) {
        Optional<Review> existingReview = reviewRepository.findByUserIdAndOrdersId(userId, ordersId);

        return existingReview.isPresent(); // 리뷰가 존재하면 true, 없으면 false 반환
    }

    public Boolean isCartExists(Integer userId) {
        Optional<Cart> isCartExists = reviewRepository.existsByUserId(userId);

        return isCartExists.isPresent();
    }

    public ResponseDto findAllReviews(CustomUserDetails customUserDetails, Pageable pageable) {
        int userId = userRepository.findById(customUserDetails.getUserId()).map(User::getUserId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));


        Page<Review> reviewsPage = reviewRepository.findAllReviews(userId,pageable);
        if (reviewsPage.isEmpty()) {
            throw new NotFoundException("등록된 리뷰가 존재하지 않습니다.");
        }

        List<ReviewResponse> reviewResponseList = reviewsPage.stream().map(reviews -> {
            Integer ordersId = reviews.getOrders().getOrdersId();
            Orders orders = ordersRepository.findById(ordersId).orElseThrow(() -> new NotFoundException("order에서 주문을 찾을 수 없습니다."));
            Integer optionId = orders.getOptions().getOptionsId();
            Options options = optionsRepository.findById(optionId).orElseThrow(() -> new NotFoundException("주문한 옵션을 찾을 수 없습니다."));
            Integer productId = options.getProduct().getProductId();
            Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("주문한 상품을 찾을 수 없습니다."));
            String photoUrl = getTruePhotoUrl(product.getProductPhotoList());

            return ReviewResponse.builder()
                    .reviewId(reviews.getReviewId())
                    .reviewScore(reviews.getReviewScore())
                    .reviewContent(reviews.getReviewContent())
                    .createAt(reviews.getCreateAt())
                    .ordersId(reviews.getOrders().getOrdersId())
                    .productImg(photoUrl)
                    .price(product.getPrice())
                    .productName(product.getProductName())
                    .optionName(options.getOptionsName())
                    .brand(product.getBrand())
                    .build();
        }).collect(Collectors.toList());

        Page<ReviewResponse> responsePage = new PageImpl<>(reviewResponseList, pageable, reviewsPage.getTotalElements());

        return new ResponseDto(HttpStatus.OK.value(), "구매 정보를 성공적으로 조회했습니다.", responsePage);
    }

    public String getTruePhotoUrl(List<ProductPhoto> productPhotoList) {
        Optional<String> optionalPhotoUrl = productPhotoList.stream()
                .filter(ProductPhoto::isPhotoType)
                .map(ProductPhoto::getPhotoUrl)
                .findFirst();

        return optionalPhotoUrl.orElse("");
    }

    public ResponseDto findAllCoupon(CustomUserDetails customUserDetails) {
        User user = userRepository.findById(customUserDetails.getUserId()).orElseThrow(() -> new NotFoundException("회원가입 후 이용해 주시길 바랍니다."));
        int userId = userRepository.findById(customUserDetails.getUserId()).map(User::getUserId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));

        List<UserCoupon> userCoupons= userCouponRepository.findAllByUserId(userId);
        List<OrderCouponResponseDto> couponResponseList= userCoupons
                .stream()
                .map(c-> new OrderCouponResponseDto(
                        c.getCoupon().getCouponName(),
                        c.getCoupon().getCouponDiscount()))
                .collect(Collectors.toList());

        CouponResponse couponResponse= CouponResponse
                .builder()
                .money(user.getMoney())
                .couponResponseList(couponResponseList)
                .build();

        return new ResponseDto(HttpStatus.OK.value(), "", couponResponse);
    }

    public ResponseDto findAllQnA(CustomUserDetails customUserDetails, Pageable pageable) {
        int userId = userRepository.findById(customUserDetails.getUserId()).map(User::getUserId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));


        List<QuestionAnswer> QnA = reviewRepository.findAllQnA(userId, pageable);
        if (QnA.isEmpty()) {
            throw new NotFoundException("등록된 질문이 존재하지 않습니다.");
        }

        List<QnAResponse> qnAResponses = QnA.stream()
                .map(questionAnswer -> QnAResponse.builder()
                        .questionAnswerId(questionAnswer.getQuestionAnswerId())
                        .question(questionAnswer.getQuestion())
                        .answer(questionAnswer.getAnswer())
                        .userName(questionAnswer.getUser().getName())
                        .createdAt(questionAnswer.getCreateAt())
                        .productName(questionAnswer.getProduct().getProductName())
                        .brand(questionAnswer.getProduct().getBrand())
                        .questionStatus(questionAnswer.getQuestionStatus())
                        .build())
                .collect(Collectors.toList());

        return new ResponseDto(HttpStatus.OK.value(), "", qnAResponses);
    }
}
