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
    private String question;
    private String answer;
    private String userName;
    private String email;
    private LocalDate createdAt;
    private String productName;
    private String brand;
    private Integer questionId;
    private String questionStatus;
}
