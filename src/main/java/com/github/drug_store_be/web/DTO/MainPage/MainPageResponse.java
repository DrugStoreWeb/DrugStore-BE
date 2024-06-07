package com.github.drug_store_be.web.DTO.MainPage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class MainPageResponse {
    @JsonProperty("product_list")
    private List<MainPageProductResponse> product_list;
    @JsonProperty("main_page_ad_img")
    private MainPageAdImg main_page_ad_img;

    private int total_pages;
    private long total_elements;
    private int current_page;
    private int page_size;
}
