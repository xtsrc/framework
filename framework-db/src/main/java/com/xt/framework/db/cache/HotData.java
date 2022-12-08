package com.xt.framework.db.cache;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author tao.xiong
 * @Description 热点数据
 * @Date 2022/11/30 15:37
 */
@Data
public abstract class HotData implements Serializable {
    private static final long serialVersionUID = -8867977419098652241L;
    /**
     * 逻辑过期时间
     */
    private LocalDateTime logicExpireTime;
}
