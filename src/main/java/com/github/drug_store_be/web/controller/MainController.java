package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.main.MainService;
import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

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


//    @GetMapping(path = "/category")
//    public ResponseDto mainPageCategory(@RequestParam(value = "category", defaultValue = "", required = true) String category,String sortBy, Pageable pageable) {
//        return mainservice.CategoryPage(category, sortBy, pageable);
//    }
//
//    @GetMapping(path = "/find")
//    public ResponseDto mainPageSearch(@RequestParam(value = "keyword", defaultValue = "", required = true) String keyword, String sortBy,Pageable pageable ) {
//        return mainservice.findPage(keyword,sortBy,pageable);
//    }
}