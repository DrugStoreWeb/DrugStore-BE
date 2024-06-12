package com.github.drug_store_be.service.exceptions;

public class ProductStatusException extends RuntimeException{
    public ProductStatusException(String message) {
        super(message);
    }
}
