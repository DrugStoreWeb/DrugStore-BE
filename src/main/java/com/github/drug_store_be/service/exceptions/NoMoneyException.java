package com.github.drug_store_be.service.exceptions;

public class NoMoneyException extends RuntimeException{
    public NoMoneyException(String message) {
        super(message);
    }
}
