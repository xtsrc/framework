package com.xt.framework.demo.airthmetic;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tao.xiong
 * @Date: 2019/12/23 18:15
 * @Description: 字典序
 */
public class DictSort {
    private static List<Integer> getNext(List<Integer> list) {
        AtomicInteger frontOne = new AtomicInteger();
        frontOne.set(Integer.MIN_VALUE);
        Collections.reverse(list);
        Optional<Integer> behindOne = list.stream().filter(a -> {
            if (a < frontOne.get()) {
                return true;
            } else {
                frontOne.set(a);
                return false;
            }
        }).findFirst();
        if (!behindOne.isPresent()) {
            return list;
        }
        Collections.reverse(list);
        Integer frontIndex = list.indexOf(frontOne.get());
        Integer behindIndex = list.indexOf(behindOne.get());
        swap(list, frontIndex, behindIndex);
        list.subList(frontIndex + 1, list.size()).sort(Comparator.naturalOrder());
        return list;
    }

    private static <E> void swap(List<E> list, Integer index1, Integer index2) {
        E e = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, e);
    }
}
