package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.main.CICDtestService;
import com.github.drug_store_be.service.main.MainService;
import com.github.drug_store_be.web.DTO.MainPage.MainPageProductResponse;
import com.github.drug_store_be.web.DTO.MainPage.MainPageResponse;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/cicd")
public class CICDtestController {

    private final CICDtestService cicDtestService;

    @GetMapping
    public ResponseDto mainPage(@RequestParam(name = "sortby", defaultValue = "sales") String sortBy,
                                @PageableDefault(page = 0, size = 24) Pageable pageable) {
        MainPageResponse mainPageResponse= cicDtestService.mainpage(sortBy,pageable);
        return new ResponseDto(HttpStatus.OK.value(), "메인 페이지 조회에 성공했습니다.",mainPageResponse);
    }
}