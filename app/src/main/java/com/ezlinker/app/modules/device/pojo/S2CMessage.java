package com.ezlinker.app.modules.device.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author wangwenhai
 * @date 2019/12/14
 * File description: 服务器给客户端推送消息
 */
@Data
public class S2CMessage {
    @NotNull(message = "必须指定模块")
    private Long moduleId;
    @NotEmpty(message = "消息内容不可为空")
    private String data;
}
