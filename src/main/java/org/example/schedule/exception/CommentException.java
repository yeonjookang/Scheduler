package org.example.schedule.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.schedule.dto.response.ResponseData;

@Slf4j
@Getter
public class CommentException extends BaseException{

    public CommentException(ResponseData exceptionData) {
        super(exceptionData);
        log.info("CommentException 호출");
    }
}
