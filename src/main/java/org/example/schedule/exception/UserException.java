package org.example.schedule.exception;

import lombok.Getter;
import org.example.schedule.controller.response.ResponseData;

@Getter
public class UserException extends RuntimeException {
    private final ResponseData exceptionData;

    public UserException(ResponseData exceptionData) {
        super(exceptionData.getMessage());
        this.exceptionData = exceptionData;
    }
}
