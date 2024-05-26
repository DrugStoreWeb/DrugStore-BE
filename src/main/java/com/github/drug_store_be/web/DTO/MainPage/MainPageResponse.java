package com.github.drug_store_be.web.DTO.MainPage;

import lombok.Builder;

import java.util.List;

@Builder
public class MainPageResponse {
    private List<MainPageProductResponse> productList;
    private MainPageAdImg mainPageAdImg;
}
