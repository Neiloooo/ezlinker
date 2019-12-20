package com.ezlinker.app;

import com.ezlinker.common.query.QueryItem;
import com.ezlinker.common.query.QueryLogic;
import com.ezlinker.common.query.QueryOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ezlinker
 * @description:
 * @author: wangwenhai
 * @create: 2019-12-20 17:18
 **/
public class GraphSqlDemo {


    public static void main(String[] args) {
        List<QueryItem>queryItems=new ArrayList<>();
        QueryItem q1 = new QueryItem();
        q1.setK("name");
        q1.setO(QueryOperator.EQUAL);
        q1.setV("wwhai");
        q1.setL(QueryLogic.NONE);

        QueryItem q2 = new QueryItem();
        q2.setK("age");
        q2.setO(QueryOperator.GREATER);
        q2.setV("25");
        q2.setL(QueryLogic.NONE);

        QueryItem q3 = new QueryItem();
        q3.setK("addr");
        q3.setO(QueryOperator.NOT_EQUAL);
        q3.setV("Gansu");
        q3.setL(QueryLogic.NONE);

        queryItems.add(q1);
        queryItems.add(q2);
        queryItems.add(q3);

        System.out.println(queryItems.toString());

    }
}
