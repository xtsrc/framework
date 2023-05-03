package com.xt.framework.demo.async.reactor.observable;
import java.util.Observable;
/**
 * @author tao.xiong
 * @date 2023/4/12 16:31
 * @desc java8观察者模式
 */
public class ObserverDemo extends Observable {
    public static void main(String[] args) {
        ObserverDemo observerDemo = new ObserverDemo();
        //添加观察者
        observerDemo.addObserver((o,arg)->{
            System.out.println("数据发生变化A");
        });
        observerDemo.addObserver((o,arg)->{
            System.out.println("数据发生变化B");
        });
        observerDemo.setChanged();//将此Observable对象标记为已更改
        observerDemo.notifyObservers();//如果该对象发生了变化，则通知其所有观察者
    }
}


