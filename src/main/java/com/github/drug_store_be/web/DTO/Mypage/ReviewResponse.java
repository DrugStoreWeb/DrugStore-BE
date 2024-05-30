package com.github.drug_store_be.web.DTO.Mypage;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Integer reviewId;
    private String optionName;
    private String productImg;
    private String productName;
    private Integer reviewScore;
    private String reviewContent;
    private LocalDate createAt;
    private Integer price;
    private String brand;
    private Integer ordersId;
}


