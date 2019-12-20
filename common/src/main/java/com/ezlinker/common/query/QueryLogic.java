package com.ezlinker.common.query;

/**
 * @program: ezlinker
 * @description:
 * @author: wangwenhai
 * @create: 2019-12-20 17:38
 **/
public enum QueryLogic {
    NONE("NONE"),
    AND("AND"),
    OR("OR");

    private String operator;

    QueryLogic(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}
