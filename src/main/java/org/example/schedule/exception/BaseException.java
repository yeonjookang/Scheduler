package org.example.schedule.exception;

import lombok.Getter;
import org.example.schedule.dto.response.ResponseData;

@Getter
public class BaseException extends RuntimeException{
    private final ResponseData exceptionData;

    public BaseException(ResponseData exceptionData) {
        super(exceptionData.getMessage());
        this.exceptionData = exceptionData;
    }

}
