package com.ezlinker.common.query;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @program: ezlinker
 * @description:
 * @author: wangwenhai
 * @create: 2019-12-20 17:37
 **/
@Data
public class QueryItem {
    private QueryLogic l = QueryLogic.NONE;
    private String k, v;
    private QueryOperator o;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
