package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.main.MainService;
import com.github.drug_store_be.web.DTO.MainPage.MainPageProductResponse;
import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/main")
public class MainController {

    private final MainService mainservice;

    @GetMapping(path = "/")
    public ResponseDto mainPage(@RequestParam(defaultValue = "sales") String sortBy) {
        MainPageResponse mainPageResponse=mainservice.mainpage(sortBy);
        return new ResponseDto(HttpStatus.OK.value(), "메인 페이지 조회에 성공했습니다.",mainPageResponse);
    }


    @GetMapping(path = "/category")
    public ResponseDto mainPageCategory(@RequestParam(value = "category", defaultValue = "1", required = true) int category,String sortBy, org.springframework.data.domain.Pageable pageable) {
        Page<MainPageProductResponse> mainPageProductResponse=mainservice.CategoryPage(category, sortBy, pageable);
        return new ResponseDto(HttpStatus.OK.value(),"카테고리 페이지 조회에 성공했습니다.",mainPageProductResponse);
    }

    @GetMapping(path = "/find")
    public ResponseDto mainPageSearch(@RequestParam(value = "keyword", defaultValue = "", required = true) String keyword, String sortBy, org.springframework.data.domain.Pageable pageable ) {
        Page<MainPageProductResponse> mainPageProductResponse=mainservice.findPage(keyword,sortBy,pageable);
        return new ResponseDto(HttpStatus.OK.value(),"검색에 성공했습니다.",mainPageProductResponse);
    }
}