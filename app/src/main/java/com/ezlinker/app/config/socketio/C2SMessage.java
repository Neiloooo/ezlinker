package com.ezlinker.app.config.socketio;

import com.ezlinker.app.modules.module.pojo.DataArea;
import com.ezlinker.app.modules.module.pojo.DataAreaValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ezlinker
 *
 * @author wangwenhai
 * @description 客户端消息
 * @create 2019-11-27 22:31
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class C2SMessage extends XWSMsg{

    private Object data;

}
