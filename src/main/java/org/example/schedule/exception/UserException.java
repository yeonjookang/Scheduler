package org.example.schedule.exception;

import lombok.Getter;
import org.example.schedule.dto.response.ResponseData;

@Getter
public class UserException extends BaseException {

    public UserException(ResponseData exceptionData) {
        super(exceptionData);
    }
}
