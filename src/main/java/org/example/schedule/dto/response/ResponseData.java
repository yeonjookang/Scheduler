package org.example.schedule.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public  enum ResponseData {

    SUCCESS(1000, HttpStatus.OK.value(), "요청에 성공하였습니다."),
    BAD_REQUEST(2000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 요청입니다."),
    URL_NOT_FOUND(2001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 URL 입니다."),
    METHOD_NOT_ALLOWED(2002, HttpStatus.METHOD_NOT_ALLOWED.value(), "유효하지 않은 HTTP 메소드입니다."),
    REQUEST_DATA_INAPPROPRIATE(2003, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 요청값입니다."),
    USER_NOT_FOUND(2004, HttpStatus.BAD_REQUEST.value(), "이메일 혹은 비밀번호를 다시 확인해주세요."),
    USER_ALREADY_EXIST(2005, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 이메일입니다."),
    COMMENT_SCHEDULE_NOT_MATCH(2006, HttpStatus.BAD_REQUEST.value(), "해당 일정에 존재하지 않는 댓글입니다."),
    SCHEDULE_NOT_FOUND(2007,HttpStatus.BAD_REQUEST.value(),"존재하지 않는 일정입니다." ),
    USER_NOT_ALLOWED(2008, HttpStatus.BAD_REQUEST.value(), "수정 및 삭제에 대한 권한이 없습니다."),
    COMMENT_NOT_FOUND(2009,HttpStatus.BAD_REQUEST.value(),"존재하지 않는 댓글입니다."),
    SERVER_ERROR(3000, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 오류가 발생하였습니다.");

    private final int code;
    private final int status;
    private final String message;
}
