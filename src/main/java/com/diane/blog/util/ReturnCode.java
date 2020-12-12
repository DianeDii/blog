package com.diane.blog.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dianedi
 * @date 2020/12/12 13:44
 * @Destription
 */
@Getter
@AllArgsConstructor
public enum ReturnCode {
    SUCCESS(0, "OK"),
    FAILED(-1, "Failed");

    private final int code;
    private final String message;
}