package com.xuecheng.base.execption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 全局异常处理器
 * @date 2023/1/29 20:56
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(XueChengPlusException e) {
        log.error("系统异常: {}", e.getErrMessage(), e);
        return new RestErrorResponse(e.getErrMessage());
    }

    //捕获不可预知异常 Exception
    @ResponseBody//将信息返回为 json格式
    @ExceptionHandler(Exception.class)//此方法捕获Exception异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//状态码返回500
    public RestErrorResponse doException(Exception e){
        log.error("捕获异常：{}",e.getMessage());
        e.printStackTrace();
        return new RestErrorResponse(CommonError.UNKNOWN_ERROR.getErrMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuffer errMsg = new StringBuffer();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(error -> {
            errMsg.append(error.getDefaultMessage()).append(",");
        });

        log.error(errMsg.toString());
        return new RestErrorResponse(errMsg.toString());
    }
}
