package com.github.drug_store_be.web.DTO.MainPage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainPageResponse {
    @JsonProperty("productList")
    private List<MainPageProductResponse> productList;
    @JsonProperty("mainPageAdImg")
    private MainPageAdImg mainPageAdImg;
}
