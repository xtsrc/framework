package com.xt.framwork.common.core.exception;

/**
 * @author tao.xiong
 * @Description 异常处理枚举类
 * @Date 2022/7/11 15:58
 */
public enum ExceptionEnum implements BaseErrorInfoInterface {
    // 数据操作错误定义
    SUCCESS(2000, "成功!"),
    BODY_NOT_MATCH(4000, "请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH(4001, "请求的数字签名不匹配!"),
    NOT_FOUND(4004, "未找到该资源!"),
    INTERNAL_SERVER_ERROR(5000, "服务器内部错误!"),
    SERVER_BUSY(5003, "服务器正忙，请稍后再试!");

    /**
     * 错误码
     */
    private final int resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    ExceptionEnum(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public int getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
