package com.xt.framework.demo.designmode.proxy.dynamic;

import com.xt.framework.demo.designmode.proxy.statics.LogitechMouse;
import com.xt.framework.demo.designmode.proxy.statics.Mouse;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author tao.xiong
 * @Description CGLIB 继承思想
 * @Date 2022/12/19 11:23
 */
public class CGLIBProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    private Object object;

    public CGLIBProxy(Object object) {
        this.object = object;
    }

    public Object getProxy() {
        //设置需要创建子类的类
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback(this);
        //创建代理对象
        return enhancer.create();
    }

    // o: cglib 动态生成的代理类的实例
    // method:实体类所调用的都被代理的方法的引用
    // objects 参数列表
    // methodProxy:生成的代理类对方法的代理引用
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("售前了解");
        Object invoke = method.invoke(object, objects);
        System.out.println("售后处理");
        return invoke;
    }

    public static void main(String[] args) {
        Mouse logitechMouse = new LogitechMouse();
        CGLIBProxy cglibProxy = new CGLIBProxy(logitechMouse);
        Mouse proxy = (Mouse) cglibProxy.getProxy();
        proxy.sell();
    }
}

