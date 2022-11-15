package com.xt.framework.demo.designmode.decorator;

/**
 * @Author: tao.xiong
 * @Date: 2019/11/19 11:04
 * @Description: concreteComponent 类
 */
public class ChickenBurger extends Humburger {
    public ChickenBurger(){
        name="鸡腿堡";
    }

    @Override
    public double getPrice() {
        return 10;
    }
}
