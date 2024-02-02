package com.xuecheng.base.exception;

/**
 * 通用错误信息
 */
public enum CommonError {
    UNKOWN_ERROR("执行过程异常，请重试"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("对象为空"),
    QUERY_NULL("查询结果为空"),
    REQUEST_NULL("请求参数为空");

    private String message;

    public String getMessage() {
        return message;
    }

    CommonError(String errMessage) {
        this.message = errMessage;
    }
}
