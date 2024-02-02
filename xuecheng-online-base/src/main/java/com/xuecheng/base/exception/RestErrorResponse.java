package com.xuecheng.base.exception;

import java.io.Serializable;

/**
 * 错误响应信息统一返回类型
 */
public class RestErrorResponse implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RestErrorResponse(String message) {
        this.message = message;
    }
}
