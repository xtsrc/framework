package com.xt.framework.demo.designmode.decorator;

/**
 * @Author: tao.xiong
 * @Date: 2019/11/19 11:10
 * @Description: concreteDecorator 类
 */
public class Lettuce extends Condiment {
    Humburger humburger;

    public Lettuce(Humburger humburger){
        this.humburger=humburger;
    }
    @Override
    public String getName() {
        return humburger.getName()+"加生菜";
    }

    @Override
    public double getPrice() {
        return humburger.getPrice()+1.5;
    }
}
