package com.diane.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dianedi
 * @date 2020/12/12 13:55
 * @Destription 拦截器
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Apiresponse handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        return Apiresponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Apiresponse handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Apiresponse.fail(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Apiresponse handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return Apiresponse.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Apiresponse handleRuntimeException(RuntimeException e)
    {
        log.error(e.getMessage(), e);
        return Apiresponse.fail(e.getMessage());
    }
}
