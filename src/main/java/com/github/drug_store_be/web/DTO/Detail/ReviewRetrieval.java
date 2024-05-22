package com.github.drug_store_be.web.DTO.Detail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.drug_store_be.repository.review.Review;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewRetrieval {
    private String nickname;
    private Integer reviewScore;
    private String reviewContent;
    private String productName;
    private LocalDate createAt;

    public ReviewRetrieval(Review review) {
        this.nickname = review.getUser().getNickname();
        this.reviewScore = review.getReviewScore();
        this.reviewContent = review.getReviewContent();
        this.productName=review.getProduct().getProductName();
        this.createAt = review.getCreateAt();
    }
}
