package com.github.drug_store_be.web.DTO.order;

import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegisterDto {
    private Integer categoryId;
    private String productName;
    private String brand;
    private Integer price;
    private Integer productDiscount;
    private Boolean best;
    private Boolean productStatus;
    private List<ProductPhoto> productPhotoList;
    private List<Options> optionsList;
}
