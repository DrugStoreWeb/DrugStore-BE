package com.github.drug_store_be.web.advice;

import com.github.drug_store_be.service.exceptions.CAuthenticationEntryPointException;
import com.github.drug_store_be.service.exceptions.InvalidValueException;
import com.github.drug_store_be.service.exceptions.NotAcceptException;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException nfe){
        log.error("Client 요청이후 DB 검색 중 에러로 다음처럼 출력합니다. " + nfe.getMessage());
        return nfe.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotAcceptException.class)
    public String handleNotAcceptException(NotAcceptException nae){
        log.error("Client 요청이 모종의 이유로 거부됩니다. " + nae.getMessage());
        return nae.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidValueException.class)
    public String handleInvalidValueException(InvalidValueException ive){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ive.getMessage());
        return ive.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ade){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ade.getMessage());
        return ade.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public String handleAuthenticationException(CAuthenticationEntryPointException ae){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ae.getMessage());
        return ae.getMessage();
    }
}
