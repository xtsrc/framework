package com.xt.framwork.common.core.exception;

/**
 * @author tao.xiong
 * @Description 服务接口类
 * @Date 2022/7/11 15:55
 */
public interface BaseErrorInfoInterface {
    /**
     * 返回
     * @return 错误码
     */
    int getResultCode();

    /**
     * 返回
     * @return 错误描述
     */
    String getResultMsg();
}
