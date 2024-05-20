package com.github.drug_store_be.web.DTO.Detail;

import com.github.drug_store_be.repository.option.Options;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductOption {
    private String option;

    public ProductOption(Options options) {
        this.option = options.getOptionsName();
    }
}
