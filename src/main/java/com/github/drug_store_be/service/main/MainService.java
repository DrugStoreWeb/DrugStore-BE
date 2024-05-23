package com.github.drug_store_be.service.main;

import com.github.drug_store_be.web.DTO.ResponseDto;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class MainService {
    public ResponseDto mainpage(String sortBy, Pageable pageable) {
    }

    public ResponseDto CategoryPage(String category, Pageable pageable) {
    }

    public ResponseDto findPage(String keyword, Pageable pageable) {
    }
}
