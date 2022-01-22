package com.xt.framwork.common.core.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tao.xiong
 * @Description 返回
 * @Date 2021/7/28 18:21
 */
@Data
public class Response implements Serializable {
    private static final long serialVersionUID = -5114142157784526576L;
    protected static final int OK_RESPONSE_VALUE = 200;
    protected static final String OK_RESPONSE_MESSAGE = "success";
    public static final Response OK_RESPONSE = new Response();
    @ApiModelProperty(
            value = "错误代码, 200表示成功",
            required = true
    )
    private int code;
    @ApiModelProperty("用户可读的错误消息")
    private String mes;
    @ApiModelProperty("请求ID")
    private String requestId;

    public Response() {
        this.code = 200;
        this.mes = "success";
    }

    public static Response success() {
        return OK_RESPONSE;
    }

    public static Response ok(String mes) {
        return new Response(200, mes);
    }

    public static Response error(int code, String mes) {
        return new Response(code, mes);
    }

    public Response(Integer code, String mes) {
        if (code == null) {
            throw new NullPointerException("the 'code' argument must not be null or empty");
        } else {
            this.code = code;
            this.mes = mes;
        }
    }

    public boolean isOk() {
        return 200 == this.code;
    }
}
