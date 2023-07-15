package com.example.lmsbackend.multitenancy.advice;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.enums.StatusCode;
import com.example.lmsbackend.multitenancy.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TenantExceptionHandler {

    @ExceptionHandler(value = {DomainAlreadyExistsException.class})
    public ResponseEntity<BaseResponse> handleDomainAlreadyExistsException(DomainAlreadyExistsException exception){
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.DOMAIN_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {CannotCreateDomainException.class})
    public ResponseEntity<BaseResponse> handleCannotCreateDomainException(CannotCreateDomainException exception){
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.DOMAIN_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {VerifyUrlInvalidException.class})
    public ResponseEntity<BaseResponse> handleVerifyUrlInvalidException(VerifyUrlInvalidException exception){
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.DOMAIN_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(value = {IncorrectPasswordException.class})
    public ResponseEntity<BaseResponse> handleIncorrectPasswordException(IncorrectPasswordException exception){
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.INCORRECT_PASSWORD);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<BaseResponse> handleUserNotFoundException(UserNotFoundException exception){
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.USER_NOT_FOUND);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {PackageNotExistsException.class})
    public ResponseEntity<BaseResponse> handlePackageNotExistsException(PackageNotExistsException exception){
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.USER_NOT_FOUND);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


}
