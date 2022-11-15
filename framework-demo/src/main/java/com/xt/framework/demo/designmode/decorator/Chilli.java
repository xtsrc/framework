package com.xt.framework.demo.designmode.decorator;

/**
 * @Author: tao.xiong
 * @Date: 2019/11/19 11:15
 * @Description: concreteDecorator 类
 */
public class Chilli extends Condiment{
    Humburger humburger;

    public Chilli(Humburger humburger){
        this.humburger=humburger;
    }
    @Override
    public String getName() {
        return humburger.getName()+"加辣椒";
    }

    @Override
    public double getPrice() {
        /**
         * 辣椒是免费的
         */
        return humburger.getPrice();
    }
}
