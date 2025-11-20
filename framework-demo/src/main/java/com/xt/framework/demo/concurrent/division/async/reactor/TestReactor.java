package com.xt.framework.demo.concurrent.division.async.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author tao.xiong
 * @date 2023/4/12 17:50
 * @desc
 */
public class TestReactor {
    public static void main(String[] args) {
        //just()：创建Flux序列，并声明数据流，
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);

        //generate
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);


        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if( list.size() ==10 ) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);

        //create
        Flux.create(sink -> {
            for(int i = 0; i < 10; i ++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);



        //发出0-1个元素 处理响应流中单个值
        Mono.just("bole").subscribe(System.out::println);
        Mono.empty().subscribe(System.out::println);
    }
}

