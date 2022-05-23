package com.xt.framework.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author tao.xiong
 * @Description 日志测试
 * @Date 2022/5/12 16:26
 */
@Slf4j
public class LogTest  extends FrameworkDbApplicationTest {
    @Test
    public void testLog(){
        log.debug("debug 测试");
        log.info("info 测试");
        log.error("error 测试");
        log.warn("warn 测试");
    }
}
