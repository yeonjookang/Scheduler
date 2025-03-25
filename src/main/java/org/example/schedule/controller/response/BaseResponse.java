package org.example.schedule.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

import static org.example.schedule.controller.response.ResponseData.SUCCESS;

@Getter
@JsonPropertyOrder({"code","status","message","result","timestamp"})
public class BaseResponse<T> {
    private final int code;
    private final int status;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    private final LocalDateTime timestamp;

    public BaseResponse(ResponseData responseData, T data) {
        this.code = responseData.getCode();
        this.status = responseData.getStatus();
        this.message = responseData.getMessage();
        this.result = data;
        this.timestamp = LocalDateTime.now();
    }

    public BaseResponse(ResponseData responseData, T data, String message) {
        this.code = responseData.getCode();
        this.status = responseData.getStatus();
        this.message = message;
        this.result = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> BaseResponse<T> successOf(T data) {
        return new BaseResponse<>(SUCCESS, data);
    }

    public static <T>  BaseResponse<T> success() {
        return new BaseResponse<>(SUCCESS, null);
    }

    public static <T> BaseResponse<T> failOf(ResponseData errorStatus) {
        return new BaseResponse<>(errorStatus, null);
    }

    public static <T> BaseResponse<T> failOf(ResponseData errorStatus, String errorMessage) {
        return new BaseResponse<>(errorStatus, null, errorMessage);
    }
}
