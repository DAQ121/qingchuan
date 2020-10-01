package com.qingchuan.coderman.web.exception;

import com.qingchuan.coderman.web.myenums.CustomizeErrorCode;

/**
 * 异常处理
 */
public class BizException extends  RuntimeException{
    @Override
    public String getMessage() {
        return super.getMessage();
    }
    public BizException(CustomizeErrorCode customizeErrorCode) {
        super(customizeErrorCode.getMessage());
    }
}
