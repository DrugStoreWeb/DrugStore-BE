package com.github.drug_store_be.service.exceptions;

import lombok.Getter;

@Getter
public class StorageUpdateFailedException extends RuntimeException{
    private final String detailMessage;
    private final String request;

    public StorageUpdateFailedException(String detailMessage, String request) {
        this.detailMessage = detailMessage;
        this.request = request;
    }
}
