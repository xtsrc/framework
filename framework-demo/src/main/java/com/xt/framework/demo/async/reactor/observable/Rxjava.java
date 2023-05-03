package com.xt.framework.demo.async.reactor.observable;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * @author tao.xiong
 * @date 2023/4/12 17:03
 * @desc RxJava 响应式编程
 */
@Slf4j
public class Rxjava {
    public static void main(String[] args) {
        Observable<Integer> observable=Observable.create(subscriber -> {
            for(int i=0;i<5;i++){
                subscriber.onNext(i);
            }
            subscriber.onCompleted();
        });
        Subscription subscribe = observable.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                log.info( "完成");
            }

            @Override
            public void onError(Throwable e) {
                log.info("异常");
            }

            @Override
            public void onNext(Integer integer) {
                log.info("接收Obsverable中发射的值：" + integer);
            }
        });
    }
}
