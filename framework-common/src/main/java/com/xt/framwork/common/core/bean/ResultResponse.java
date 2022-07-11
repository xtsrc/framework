package com.xt.framwork.common.core.bean;

import com.xt.framwork.common.core.exception.BaseErrorInfoInterface;
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
public class ResultResponse<T> extends Response {
    private static final long serialVersionUID = -6394556731977143569L;
    private static final ResultResponse<Void> EMPTY_OK_RESULT = new ResultResponse<>();
    @ApiModelProperty("业务数据")
    private T result;

    public static ResultResponse<Void> success() {
        return EMPTY_OK_RESULT;
    }

    public static <T> ResultResponse<T> fail(String mes) {
        ResultResponse<T> resultResponse = new ResultResponse<>();
        resultResponse.setCode(402);
        resultResponse.setMes(mes);
        return resultResponse;
    }

    public static <T> ResultResponse<T> fail(int code, String mes) {
        ResultResponse<T> resultResponse = new ResultResponse<>();
        resultResponse.setCode(code);
        resultResponse.setMes(mes);
        return resultResponse;
    }

    public static <T> ResultResponse<T> fail(BaseErrorInfoInterface baseErrorInfoInterface) {
        ResultResponse<T> resultResponse = new ResultResponse<>();
        resultResponse.setCode(baseErrorInfoInterface.getResultCode());
        resultResponse.setMes(baseErrorInfoInterface.getResultMsg());
        return resultResponse;
    }

    public static <T> ResultResponse<T> success(T data) {
        ResultResponse<T> resultResponse = new ResultResponse<>();
        resultResponse.setCode(200);
        resultResponse.setMes("success");
        resultResponse.setResult(data);
        return resultResponse;
    }
}
