package com.ezlinker.common.query;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @program: ezlinker
 * @description:
 * @author: wangwenhai
 * @create: 2019-12-20 17:37
 **/
@Data
public class QueryItem {
    /**
     * 逻辑子句
     */
    private QueryLogic l = QueryLogic.NONE;
    /**
     * 查询的参数--值
     */

    @NotEmpty(message = "列不可为空")
    private String k;
    @NotEmpty(message = "值不可为空")
    private String v;
    /**
     * 查询操作符
     */
    @NotNull(message = "操作符不可为空")
    private QueryOperator o;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
