package com.github.drug_store_be.web.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponseDto {
    private String userName;
    private String phoneNumber;
    private String address;
    private String ordersNumber;
    private LocalDate ordersAt;
    private List<OrderCouponResponseDto> orderCouponList;
    private List<OrderProductResponseDto> orderProductList;
}
