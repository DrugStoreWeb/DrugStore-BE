package com.github.drug_store_be.web.advice;

import com.github.drug_store_be.service.exceptions.*;
import com.github.drug_store_be.web.DTO.ResponseDto;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ResponseDto> handleMessagingException(MessagingException messageE){
        log.error("Client 요청이후 DB 검색 중 에러로 다음처럼 출력합니다. " + messageE.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), "이메일 전송 중 에러 발생");
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
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

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ResponseDto> handleNotAuthorizedException(NotAuthorizedException nae){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + nae.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.FORBIDDEN.value(), nae.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyException.class)
    public ResponseEntity<ResponseDto> handleEmptyException(EmptyException ee){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ee.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.NOT_FOUND.value(), ee.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(StorageUpdateFailedException.class)
    public ResponseEntity<ResponseDto> handleFileUploadFailedException(StorageUpdateFailedException sufe){
        log.error("StorageUpdateFailedException: " + sufe.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), sufe.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ResponseDto> handleNotEnoughStockException(NotEnoughStockException nee){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + nee.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), nee.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SoldOutException.class)
    public ResponseEntity<ResponseDto> handleSoldOutException(SoldOutException soe){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + soe.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), soe.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductStatusException.class)
    public ResponseEntity<ResponseDto> handleProductStatusException(ProductStatusException pse){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + pse.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), pse.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoMoneyException.class)
    public ResponseEntity<ResponseDto> handleNoMoneyException(NoMoneyException nme){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + nme.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), nme.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseDto> handleDataAccessException(DataAccessException dae){
        log.error("DataAccessException : " + dae.getMessage());
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), dae.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

}
