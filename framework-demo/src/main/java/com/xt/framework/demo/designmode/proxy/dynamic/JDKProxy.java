package com.xt.framework.demo.designmode.proxy.dynamic;

import com.xt.framework.demo.designmode.proxy.statics.LogitechMouse;
import com.xt.framework.demo.designmode.proxy.statics.Mouse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author tao.xiong
 * @Description jdk实现接口思想
 * @Date 2022/12/19 11:09
 */
public class JDKProxy implements InvocationHandler {

    private Object object;

    public JDKProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("售前了解");
        Object invoke = method.invoke(object, args);
        System.out.println("售后服务");
        return invoke;
    }

    public static void main(String[] args) {
        Mouse logitechMouse = new LogitechMouse();
        JDKProxy jdkProxy = new JDKProxy(logitechMouse);
        Mouse mouse= (Mouse) Proxy.newProxyInstance(jdkProxy.getClass().getClassLoader(), new Class[]{Mouse.class}, jdkProxy);
        mouse.sell();
    }
}

