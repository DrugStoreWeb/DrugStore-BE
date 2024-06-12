package com.github.drug_store_be.service.exceptions;

public class SoldOutException extends RuntimeException{
    public SoldOutException(String message) {
        super(message);
    }
}
