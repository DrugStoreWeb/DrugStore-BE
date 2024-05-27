package com.github.drug_store_be.web.DTO.Mypage;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersResponse {
    private Integer ordersId;
    private List<String> productImg;
    private Integer price;
    private String productName;
    private String optionName;
    private String brand;
    private boolean review_status;
    private LocalDate review_deadline;
}
