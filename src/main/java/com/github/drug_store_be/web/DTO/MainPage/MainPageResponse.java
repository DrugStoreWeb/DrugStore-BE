package com.github.drug_store_be.web.DTO.MainPage;

import lombok.Builder;
import java.util.List;

@Builder
public class MainPageResponse {
    private Integer product_id;
    private String product_name;
    private String brand_name;
    private Integer price;
    private Integer final_price;
    private List<String> product_img;
    private boolean likes;
    private boolean best;
    private boolean sales;
}
