package com.github.drug_store_be.web.advice;

import com.github.drug_store_be.service.exceptions.CAuthenticationEntryPointException;
import com.github.drug_store_be.service.exceptions.InvalidValueException;
import com.github.drug_store_be.service.exceptions.NotAcceptException;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> handleNotFoundException(NotFoundException nfe){
        log.error("Client 요청이후 DB 검색 중 에러로 다음처럼 출력합니다. " + nfe.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.NOT_FOUND.value(), nfe.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotAcceptException.class)
    public ResponseEntity<ResponseDto> handleNotAcceptException(NotAcceptException nae){
        log.error("Client 요청이 모종의 이유로 거부됩니다. " + nae.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), nae.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ResponseDto> handleInvalidValueException(InvalidValueException ive){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ive.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), ive.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDto> handleAccessDeniedException(AccessDeniedException ade){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ade.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.FORBIDDEN.value(), ade.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public ResponseEntity<ResponseDto> handleAuthenticationException(CAuthenticationEntryPointException ae){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ae.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.UNAUTHORIZED.value(), ae.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }
}
