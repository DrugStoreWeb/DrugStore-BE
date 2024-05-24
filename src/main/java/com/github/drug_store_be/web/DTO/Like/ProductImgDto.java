package com.github.drug_store_be.web.DTO.Like;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductImgDto {
    private String photoUrl;
    private boolean photoType;
}
