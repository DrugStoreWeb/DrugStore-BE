package com.github.drug_store_be.web.DTO.MainPage;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MainPageAdImg {
    private String review_top_image_url;
    private String sales_top_image_url;
    private String likes_top_image_url;

}
