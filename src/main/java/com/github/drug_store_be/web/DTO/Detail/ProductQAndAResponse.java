package com.github.drug_store_be.web.DTO.Detail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductQAndAResponse {
    //            "question" : "언제 입고되나요",
    //                "answer" : null,
    //                "user_name" : "jieun",
    //                "created_at" : "2024-05-27",
    //                "product_name" : "어노브 대용량 딥 데미지 트리트먼트",
    //                "brand": "어노브",
    //                "question_id" : 1,
    //                "question_status" : "답변대기" 0 = 비활성(답변대기), 1 = 활성(답변완료)
    //         }
    private String question;
    private String answer;
    private String userName;
    private LocalDate createdAt;
    private String productName;
    private String brand;
    private Integer questionId;
    private boolean iaAnswer;
}
