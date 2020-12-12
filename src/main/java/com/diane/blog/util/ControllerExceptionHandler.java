package com.diane.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dianedi
 * @date 2020/12/12 13:55
 * @Destription
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ApiResponse handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ApiResponse handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ApiResponse.fail(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return ApiResponse.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ApiResponse handleRuntimeException(RuntimeException e)
    {
        log.error(e.getMessage(), e);
        return ApiResponse.fail(e.getMessage());
    }
}
