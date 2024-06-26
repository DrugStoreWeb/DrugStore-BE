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
    private String productImg;
    private Integer price;
    private String productName;
    private String optionName;
    private String brand;
    private boolean reviewStatus;
    private LocalDate reviewDeadline;
}
