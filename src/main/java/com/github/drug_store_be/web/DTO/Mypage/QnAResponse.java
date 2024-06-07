package com.github.drug_store_be.web.DTO.Mypage;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QnAResponse {
    private String question;
    private String answer;
    private String userName;
    private LocalDate createdAt;
    private String productName;
    private String brand;
    private Integer questionAnswerId;
    private Boolean questionStatus;
}
