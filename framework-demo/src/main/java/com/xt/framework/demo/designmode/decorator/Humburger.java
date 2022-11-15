package com.xt.framework.demo.designmode.decorator;

/**
 * @Author: tao.xiong
 * @Date: 2019/11/19 11:01
 * @Description: component 类
 */
public abstract class Humburger {
    protected String name;

    public String getName() {
        return name;
    }
    public abstract double getPrice();
}
