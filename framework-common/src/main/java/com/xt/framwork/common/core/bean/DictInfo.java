package com.xt.framwork.common.core.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tao.xiong
 * @Description 字典信息
 * @Date 2022/5/11 16:27
 */
@Data
public class DictInfo implements Serializable {
    private static final long serialVersionUID = 6067870134879580623L;
    private String key;
    private String value;
}
