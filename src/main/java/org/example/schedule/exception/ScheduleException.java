package org.example.schedule.exception;

import lombok.Getter;
import org.example.schedule.controller.response.ResponseData;

@Getter
public class ScheduleException extends RuntimeException{
    private final ResponseData exceptionData;

    public ScheduleException(ResponseData exceptionData) {
        super(exceptionData.getMessage());
        this.exceptionData = exceptionData;
    }
}
