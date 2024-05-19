package com.github.drug_store_be.service.exceptions;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String message) {
        super(message);
    }
}
