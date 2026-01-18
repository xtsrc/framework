package com.xt.framework.demo.designmode.proxy.statics;

/**
 * @author tao.xiong
 * @Description 代理
 * @Date 2022/12/19 11:02
 */
public class MouseProxy implements Mouse {
    private Mouse mouse;

    public MouseProxy(Mouse mouse) {
        this.mouse = mouse;
    }

    @Override
    public void sell() {
        System.out.println("售前了解");
        mouse.sell();
        System.out.println("售后服务");
    }

    public static void main(String[] args) {
        Mouse logitechMouse = new LogitechMouse();
        MouseProxy mouseProxy = new MouseProxy(logitechMouse);
        mouseProxy.sell();
    }
}
