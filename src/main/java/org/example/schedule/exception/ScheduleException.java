package org.example.schedule.exception;

import lombok.Getter;
import org.example.schedule.dto.response.ResponseData;

@Getter
public class ScheduleException extends BaseException{

    public ScheduleException(ResponseData exceptionData) {
        super(exceptionData);
    }
}
