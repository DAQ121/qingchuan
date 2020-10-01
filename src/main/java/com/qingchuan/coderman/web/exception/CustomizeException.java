package com.qingchuan.coderman.web.exception;

import com.qingchuan.coderman.web.myenums.CustomizeErrorCode;

/**
 * 自定义异常并抛出
 */
public class CustomizeException extends RuntimeException {

    @Override
    public String getMessage() {
        return super.getMessage();
    }
    public CustomizeException(CustomizeErrorCode customizeErrorCode) {
        super(customizeErrorCode.getMessage());
    }
}
