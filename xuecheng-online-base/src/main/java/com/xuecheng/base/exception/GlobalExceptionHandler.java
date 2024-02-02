package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     * @param xuechengException 自定义异常
     * @return
     */
    @ExceptionHandler(XuechengException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse handleCustomException(XuechengException xuechengException){
        // 记录日志
        log.error("捕获到全局异常:{}",xuechengException.getMessage(),xuechengException);
        return new RestErrorResponse(xuechengException.getMessage());
    }

    /**
     * 处理其他异常
     * @param e  其他异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse handleException(Exception e){
        log.error("捕获到全局异常:{}",e.getMessage(),e);
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getMessage());
    }

    /**
     * 处理MethodArgumentNotValidException异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("捕获到全局异常:{}",e.getMessage(),e);
        BindingResult bindingResult = e.getBindingResult();
        List<String> errMessages= new ArrayList<>();  //用于存放错误信息
        bindingResult.getFieldErrors().stream().forEach(item->
                errMessages.add(item.getDefaultMessage()));
        //拼接错误信息
        String s = StringUtils.join(errMessages, ",");
        return new RestErrorResponse(s);
    }

}
