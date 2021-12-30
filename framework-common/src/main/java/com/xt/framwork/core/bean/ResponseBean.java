package com.xt.framwork.core.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tao.xiong
 * @Description
 * @Date 2021/7/28 18:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseBean<T> extends Response {
    private static final long serialVersionUID = -6394556731977143569L;
    private static final ResponseBean<Void> EMPTY_OK_RESULT = new ResponseBean<>();
    @ApiModelProperty("业务数据")
    private T data;

    public static ResponseBean<Void> success() {
        return EMPTY_OK_RESULT;
    }

    public static <T> ResponseBean<T> fail(String mes) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setCode(402);
        responseBean.setMes(mes);
        return responseBean;
    }

    public static <T> ResponseBean<T> fail(int code, String mes) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setCode(code);
        responseBean.setMes(mes);
        return responseBean;
    }

    public static <T> ResponseBean<T> success(T data) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setCode(200);
        responseBean.setMes("success");
        responseBean.setData(data);
        return responseBean;
    }
}
