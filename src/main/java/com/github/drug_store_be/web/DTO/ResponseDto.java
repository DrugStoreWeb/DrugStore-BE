package com.github.drug_store_be.web.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDto {
    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public ResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseDto(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
