package com.github.drug_store_be.web.DTO.MainPage;


import lombok.Builder;

@Builder
public class MainPageAdImg {
    private String reviewTopImageUrl;
    private String salesTopImageUrl;
    private String likesTopImageUrl;


    public MainPageAdImg(String reviewTopImageUrl) {
    }
}
