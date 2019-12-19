package com.ezlinker.app.modules.module.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: ezlinker
 * @description: 模块上下线记录
 * @author: wangwenhai
 * @create: 2019-12-19 16:19
 **/
@Data
public class ModuleLog implements Serializable {
    // 连接
    public static Integer CONNECT = 1;
    // 掉线
    public static Integer DISCONNECT = 2;

    private Long moduleId;
    private Integer type;
    private Date createTime;

}
