package com.xt.framework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author tao.xiong
 * @Description 返回结果
 * @Date 2021/8/2 16:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ResponseBean<T> {
    private String message;
    private String status;
    private Boolean success;
    private long timestamp;
    private int total;
    private T data;

    public static <T> ResponseBean<T> fail(String message){
        ResponseBean<T> responseBean=new ResponseBean<T>();
        responseBean.setMessage(message);
        responseBean.setSuccess(false);
        return responseBean;
    }
}
