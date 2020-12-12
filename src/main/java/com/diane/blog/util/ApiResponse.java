package com.diane.blog.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Builder
@Slf4j
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;


    private T data;

    public static <T> ApiResponse success(T data) {
        ApiResponse response = ApiResponse.builder().code(ReturnCode.SUCCESS.getCode()).message(ReturnCode.SUCCESS.getMessage()).data(data).build();
        log.info("Success API Response: {}", response.toString());
        return response;
    }

    public static ApiResponse success() {
        return success(null);
    }

    public static <T> ApiResponse fail(int code, String message) {
        ApiResponse response = ApiResponse.builder().code(code).message(message).build();
        log.error("Failed API Response: {}", response.toString());
        return response;
    }

    public static <T> ApiResponse fail(ReturnCode returnCode) {
        return fail(returnCode.getCode(), returnCode.getMessage());
    }

    public static <T> ApiResponse fail() {
        return fail(ReturnCode.FAILED);
    }

    public static <T> ApiResponse fail(String message) {
        return fail(ReturnCode.FAILED.getCode(), message);
    }
}