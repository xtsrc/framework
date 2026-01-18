package com.xt.framework.demo.designmode.proxy.statics;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/12/19 11:01
 */
public class LogitechMouse implements Mouse{
    @Override
    public void sell() {
        System.out.println("出售逻辑鼠标");
    }
}
