package com.github.drug_store_be.web.DTO.Detail;

import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductImg {
    private Integer imgId;
    private Boolean imgMain;
    private String img;


    public ProductImg(ProductPhoto productPhoto) {
        this.imgId = productPhoto.getProductPhotoId();
        this.imgMain = productPhoto.getProduct().isProductStatus();
        this.img = productPhoto.getPhotoUrl();
    }
}
