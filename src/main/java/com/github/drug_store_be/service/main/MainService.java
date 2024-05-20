package com.github.drug_store_be.service.main;

import com.github.drug_store_be.web.DTO.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class MainService {
    public ResponseDto findAll(int page, int size, String sortBy) {
        return null;
    }

    public ResponseDto findByCategory(int page, int size, String sortBy, String category) {
        return null;
    }

    public ResponseDto findByProductNameOrBrand(int page, int size, String sortBy, String keyword) {
        return null;
    }
}
