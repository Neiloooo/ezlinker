package com.ezlinker.app.modules.feature.pojo;

import lombok.Data;

/**
 * ezlinker
 *
 * @author wangwenhai
 * @description 命令的值
 * @create 2019-12-07 17:10
 **/
@Data
public class CmdValue {
    /**
     * 字段名
     */
    private String field;
    /**
     * 类型:
     * 1: Number
     * 2: String
     * 3: Boolean
     * 4: JSON Format String
     */
    private Integer type;
    /**
     * 字段的默认值,默认为 :空
     */
    private String defaultValue;
}
