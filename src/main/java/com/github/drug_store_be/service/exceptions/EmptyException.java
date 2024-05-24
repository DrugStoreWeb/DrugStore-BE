package com.github.drug_store_be.service.exceptions;

public class EmptyException extends RuntimeException{
    public EmptyException(String message) {
        super(message);
    }
}
