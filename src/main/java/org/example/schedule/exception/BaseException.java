package org.example.schedule.exception;

import lombok.Getter;
import org.example.schedule.controller.response.ResponseData;

@Getter
public class BaseException extends RuntimeException{
    private final ResponseData exceptionData;

    public BaseException(ResponseData exceptionData) {
        super(exceptionData.getMessage());
        this.exceptionData = exceptionData;
    }

    public BaseException(String exceptionMessage, ResponseData exceptionData) {
        super(exceptionMessage);
        this.exceptionData = exceptionData;
    }
}
