package com.github.drug_store_be.web.DTO.Detail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductImg {
    private Integer imgId;
    private Boolean imgMain;
    private String img;


    public ProductImg(ProductPhoto productPhoto) {
        this.imgId = productPhoto.getProductPhotoId();
        this.imgMain = productPhoto.isPhotoType();
        this.img = productPhoto.getPhotoUrl();
    }
}
