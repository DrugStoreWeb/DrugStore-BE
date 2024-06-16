package com.github.drug_store_be.web.DTO.Detail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.option.OptionsRepository;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import com.github.drug_store_be.repository.productPhoto.ProductPhotoJpa;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductOption {
    private Integer optionId;
    private String option;
    private Integer optionPrice;
    private Integer optionStock;

    public ProductOption(Options options) {
        this.optionId= options.getOptionsId();
        this.option = options.getOptionsName();
        this.optionPrice =options.getOptionsPrice();
        this.optionStock=options.getStock();
    }
    public static ProductOption ConvertEntityToDto(Options options){
        return ProductOption.builder()
                .optionId(options.getOptionsId())
                .option(options.getOptionsName())
                .optionPrice(options.getOptionsPrice())
                .optionStock(options.getStock())
                .build();
    }

    public static List<ProductOption> ConvertEntityListToDtoList(Product product, OptionsRepository optionsRepository){
        List<Options> optionsByProduct = optionsRepository.findAllByProduct(product);
        return optionsByProduct.stream().map(ProductOption::new).toList();
    }
}
