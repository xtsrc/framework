package com.xt.framework.common.core.bean;

import lombok.Data;

/**
 * @author tao.xiong
 * @Description 消息实体
 * @Date 2021/8/30 15:31
 */
@Data
public class PulsarMessage<T> {
    private String id;
    private T content;
}
