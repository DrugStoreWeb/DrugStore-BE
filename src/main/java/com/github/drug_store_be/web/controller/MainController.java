package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.service.main.MainService;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseDto mainPage(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                @RequestParam(value = "size", defaultValue = "20", required = false) int size,
                                @RequestParam(value = "sort", defaultValue = "sales", required = false) String sortBy) {
        return mainservice.findAll(page, size, sortBy);
    }

    ;

    @GetMapping(path = "/category")
    public ResponseDto mainPageCategory(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                        @RequestParam(value = "size", defaultValue = "20", required = false) int size,
                                        @RequestParam(value = "sort", defaultValue = "sales", required = false) String sortBy,
                                        @RequestParam(value = "category", defaultValue = "sales", required = true) String category) {
        return mainservice.findByCategory(page, size, sortBy, category);
    }

    @GetMapping(path = "/find")
    public ResponseDto mainPageSearch(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                @RequestParam(value = "size", defaultValue = "20", required = false) int size,
                                @RequestParam(value = "sort", defaultValue = "sales", required = false) String sortBy,
                                @RequestParam(value = "keyword", defaultValue = "keyword", required = true) String keyword) {
        return mainservice.findByProductNameOrBrand(page, size, sortBy, keyword);
    }
}