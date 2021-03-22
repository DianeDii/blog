package com.diane.blog.config;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Builder
@Slf4j
public class Apiresponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;


    private T data;
//  成功不需要响应码
    public static <T> Apiresponse success(T data) {
        Apiresponse response = Apiresponse.builder().code(ReturnCode.SUCCESS.getCode()).message(ReturnCode.SUCCESS.getMessage()).data(data).build();
        log.info("Success API Response: {}", response.toString());
        return response;
    }

    public static Apiresponse success() {
        return success(null);
    }

    public static <T> Apiresponse fail(int code, String message) {
        Apiresponse response = Apiresponse.builder().code(code).message(message).build();
        log.error("Failed API Response: {}", response.toString());
        return response;
    }

    public static <T> Apiresponse fail(ReturnCode returnCode) {
        return fail(returnCode.getCode(), returnCode.getMessage());
    }

    public static <T> Apiresponse fail() {
        return fail(ReturnCode.FAILED);
    }

    public static <T> Apiresponse fail(String message) {
        return fail(ReturnCode.FAILED.getCode(), message);
    }
}