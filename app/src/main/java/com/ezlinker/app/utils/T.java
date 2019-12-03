package com.ezlinker.app.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: ezlinker
 * @description:
 * @author: wangwenhai
 * @create: 2019-11-13 09:52
 **/
public class T {
    interface I {
        Integer foreach(Integer t);
    }

    static Set<Integer> filter(List<Integer> integers, I i) {
        Set<Integer> r = new HashSet<>();
        for (Integer num : integers) {
            r.add(i.foreach(num));
        }
        return r;

    }

    public static void main(String[] args) {

        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);
        integerList.add(4);
        integerList.add(5);
        integerList.add(6);
        Set<Integer> list = T.filter(integerList, t -> {
            if (t > 3) {
                return t;
            } else {
                return 0;
            }
        });

    }
}
