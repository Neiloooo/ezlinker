package com.ezlinker.app.modules.feature.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author wangwenhai
 * @date 2019/12/28
 * File description: 指令
 */
@Data
public class Cmd {
    /**
     * 命令Key
     */
    @NotEmpty(message = "指令名不可为空值")
    private String cmdKey = "cmdKey";

    /**
     * 命令Value
     */
    private List<CmdValue> cmdValues;

}
