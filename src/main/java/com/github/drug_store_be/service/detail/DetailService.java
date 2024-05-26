package com.github.drug_store_be.service.detail;

import com.github.drug_store_be.repository.like.Likes;
import com.github.drug_store_be.repository.like.LikesJpa;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsJpa;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.product.ProductJpa;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import com.github.drug_store_be.repository.questionAnswer.QuestionAnswer;
import com.github.drug_store_be.repository.questionAnswer.QuestionAnswerJpa;
import com.github.drug_store_be.repository.review.Review;
import com.github.drug_store_be.repository.review.ReviewJpa;
import com.github.drug_store_be.repository.role.Role;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.repository.userRole.UserRole;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Detail.*;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetailService {
    private final ProductJpa productJpa;
    private final ReviewJpa reviewJpa;
    private final ProductPhotoJpa productPhotoJpa;
    private final OptionsJpa optionsJpa;
    private final UserJpa userJpa;
    private final LikesJpa likesJpa;
    private final QuestionAnswerJpa questionAnswerJpa;
    @Cacheable(value = "productDetails",key = "#productId")
    public ResponseDto productDetailResult(Integer productId, CustomUserDetails customUserDetails) {
        User user =userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()-> new NotFoundException("토큰에 해당하는 유저를 찾을 수 없습니다."));
        Product product = productJpa.findById(productId)
                .orElseThrow(()-> new NotFoundException(productId+"에 해당하는 상세 페이지를 찾을 수 없습니다."));
        List<Review> reviewListByProduct =reviewJpa.findAllByProduct(product);
        int reviewCount= reviewListByProduct.size();
        List<ProductPhoto> productPhotosByProduct=productPhotoJpa.findAllByProduct(product);
        List<Options> optionsByProduct = optionsJpa.findAllByProduct(product);
        List<ProductImg> productImgs = productPhotosByProduct.stream().map(ProductImg::new).toList();
        List<ProductOption> productOptions = optionsByProduct.stream().map(ProductOption::new).toList();
        ProductDetailResponse productDetailResponse = new ProductDetailResponse(product,productImgs,reviewCount,productOptions);
            if (likesJpa.existsByUserAndProduct(user,product)) {
                productDetailResponse.setIsLike(true);
                return new ResponseDto(HttpStatus.OK.value(), "조회 성공",productDetailResponse);
            }else {
                productDetailResponse.setIsLike(false);
                return new ResponseDto(HttpStatus.OK.value(), "조회 성공",productDetailResponse);
            }

    }
    @Cacheable(value = "productDetails",key = "#productId")
    public ResponseDto productDetailResultByNotLogin(Integer productId) {
        Product product = productJpa.findById(productId)
                .orElseThrow(()-> new NotFoundException(productId+"에 해당하는 상세 페이지를 찾을 수 없습니다."));
        List<Review> reviewListByProduct =reviewJpa.findAllByProduct(product);
        Integer reviewCount= reviewListByProduct.size();

        List<ProductPhoto> productPhotosByProduct=productPhotoJpa.findAllByProduct(product);
        List<Options> optionsByProduct = optionsJpa.findAllByProduct(product);
        List<ProductImg> productImgs = productPhotosByProduct.stream().map(ProductImg::new).toList();
        List<ProductOption> productOptions = optionsByProduct.stream().map(ProductOption::new).toList();
        ProductDetailResponse productDetailResponse = new ProductDetailResponse(product,productImgs,reviewCount,productOptions);
        productDetailResponse.setIsLike(false);
        return new ResponseDto(HttpStatus.OK.value(), "조회 성공",productDetailResponse);
    }
    @Cacheable(value = "productDetails",key = "#productId")
    public ResponseDto productReviewResult(Integer productId, Integer pageNum, String criteria) {
        Product product = productJpa.findById(productId)
                .orElseThrow(()-> new NotFoundException("해당 상품을 찾을 수 없습니다."));
        Pageable pageable = PageRequest.of(pageNum,10);
        if (criteria.equals("createAt")){
            Page<Review> reviewPage =reviewJpa.findByProductOrderByCreateAtDesc(product,pageable);
            Page<ReviewRetrieval> reviewRetrievalPage=reviewPage.map(ReviewRetrieval::new);
            return new ResponseDto(HttpStatus.OK.value(),"조회성공",reviewRetrievalPage);
        }else if (criteria.equals("reviewScoreDesc")){
            Page<Review> reviewPage =reviewJpa.findByProductOrderByReviewScoreDesc(product,pageable);
            Page<ReviewRetrieval> reviewRetrievalPage=reviewPage.map(ReviewRetrieval::new);
            return new ResponseDto(HttpStatus.OK.value(),"조회성공",reviewRetrievalPage);
        }else if (criteria.equals("reviewScoreAsc")){
            Page<Review> reviewPage =reviewJpa.findByProductOrderByReviewScoreAsc(product,pageable);
            Page<ReviewRetrieval> reviewRetrievalPage=reviewPage.map(ReviewRetrieval::new);
            return new ResponseDto(HttpStatus.OK.value(),"조회성공",reviewRetrievalPage);
        }else {
            Page<Review> reviewPage =reviewJpa.findByProductOrderByCreateAtDesc(product,pageable);
            Page<ReviewRetrieval> reviewRetrievalPage=reviewPage.map(ReviewRetrieval::new);
            return new ResponseDto(HttpStatus.OK.value(),"조회성공",reviewRetrievalPage);
        }
    }

@Transactional
@CacheEvict(value = "productDetails",allEntries = true)
    public ResponseDto answerByAdminResult(Integer questionId, CustomUserDetails customUserDetails, Answer answer) {
        String userEmail = customUserDetails.getEmail();
        User user = userJpa.findByEmailFetchJoin(userEmail)
                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        String roleName = user.getUserRole().stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .findFirst().orElseThrow(()->new NotFoundException("유저에게 역할이 없습니다."));
        if (!roleName.equals("ROLE_ADMIN")){
            throw new NotFoundException("관리자 계정만 해당 기능을 이용할 수 있습니다.");
        }
        QuestionAnswer questionAnswer = questionAnswerJpa.findById(questionId)
                .orElseThrow(()-> new NotFoundException("요청하신 Q&A는 찾을 수 없습니다."));
        try {
            questionAnswer.setAnswer(answer.getMessage());
            questionAnswer.setQuestionStatus(true);
            return new ResponseDto(HttpStatus.OK.value(), "답변 작성이 되었습니다.",answer);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "답변 작성에 실패했습니다.");
        }


    }
}
