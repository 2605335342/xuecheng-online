package com.xuecheng.base.exception;

/**
 * 自定义异常类型
 */
public class XuechengException extends RuntimeException{
    private String message;

    public String getMessage() {
        return message;
    }

    public XuechengException() {
        super();
    }

    public XuechengException(String errMessage) {
        super(errMessage);
        this.message = errMessage;
    }

    public static void cast(CommonError commonError){
        throw new XuechengException(commonError.getMessage());
    }

    public static void cast(String errMessage){
        throw new XuechengException(errMessage);
    }

}
