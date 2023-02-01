package com.xuecheng.base.execption;

import java.io.Serializable;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 错误响应参数包装
 * @date 2023/1/29 20:55
 */
public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
