package org.example.schedule.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.schedule.controller.response.BaseResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

import static org.example.schedule.controller.response.ResponseData.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(UserException.class)
    public BaseResponse<ResponseStatus> handle_UserException(UserException exception) {
        log.error("[UserExceptionControllerAdvice: handle_UserException 호출]", exception);
        return BaseResponse.failOf(exception.getExceptionData(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler({BaseException.class, NoHandlerFoundException.class, TypeMismatchException.class})
    public BaseResponse<ResponseStatus> handle_BadRequest(Exception exception) {
        log.error("[BaseExceptionControllerAdvice: handle_BadRequest 호출]", exception);
        return BaseResponse.failOf(URL_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse<ResponseStatus> handle_HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[BaseExceptionControllerAdvice: handle_HttpRequestMethodNotSupportedException 호출]", e);
        return BaseResponse.failOf(METHOD_NOT_ALLOWED);
    }

    //json 포맷 오류
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<ResponseStatus> handle_MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[BaseExceptionControllerAdvice: handle_MethodArgumentNotValidException 호출]", e);

        String errorMessages = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return BaseResponse.failOf(REQUEST_DATA_INAPPROPRIATE,errorMessages);
    }

    //쿼리 파라미터 오류
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse<ResponseStatus> handle_ConstraintViolationException(ConstraintViolationException e) {
        log.error("[BaseExceptionControllerAdvice: handle_ConstraintViolationException 호출]", e);
        return BaseResponse.failOf(REQUEST_DATA_INAPPROPRIATE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public BaseResponse<ResponseStatus> handle_RuntimeException(Exception e) {
        log.error("[BaseExceptionControllerAdvice: handle_RuntimeException 호출]", e);
        return BaseResponse.failOf(SERVER_ERROR);
    }
}
