package com.diane.blog.config;

import lombok.Getter;

/**
 * @author dianedi
 * @date 2020/12/12 13:47
 * @Destription 异常统一响应类
 */
@Getter
public class ServiceException extends RuntimeException {
    private int code;

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ReturnCode returnCode) {
        super(returnCode.getMessage());
        this.code = returnCode.getCode();
    }
}