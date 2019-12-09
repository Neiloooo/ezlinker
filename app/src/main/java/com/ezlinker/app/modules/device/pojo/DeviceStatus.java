package com.ezlinker.app.modules.device.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * ezlinker
 *
 * @author wangwenhai
 * @description 设备的当前状态
 * @create 2019-12-09 21:37
 **/
@Data
public class DeviceStatus {
    /**
     * 字段名
     */
    @NotEmpty(message = "状态字段名不可为空")
    private String field;
    /**
     * 类型:
     * 1: Number
     * 2: String
     * 3: Boolean
     * 4: JSON Format String
     */
    @NotEmpty(message = "状态字段值不可为空")

    private String value;


}
