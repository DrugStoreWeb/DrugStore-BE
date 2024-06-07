package com.github.drug_store_be.web.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRegisterDto {
    private Integer categoryId;
    private String productName;
    private String brand;
    private Integer price;
    private Integer productDiscount;
    private Boolean best;
    private Boolean productStatus;
    private List<ProductPhotoRegisterDto> productPhotoList;
    private List<OptionsRegisterDto> optionsList;
}
