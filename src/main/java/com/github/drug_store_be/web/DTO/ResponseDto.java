package com.github.drug_store_be.web.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ResponseDto {
    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public ResponseDto() {
        this.code = HttpStatus.OK.value();
        this.message = HttpStatus.OK.name();
    }

    public ResponseDto(int code, String message) {
        this.code =code;
        this.message = message;
    }

    public ResponseDto(Object data) {
        this.code = HttpStatus.OK.value();
        this.message = HttpStatus.OK.name();
        this.data = data;
    }

    public ResponseDto(int code, String message, Object data) {
        this.code =code;
        this.message = message;
        this.data= data;
    }

}
