package com.ezlinker.app.config.socketio;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: ezlinker
 * @description: 客户端上下线
 * @author: wangwenhai
 * @create: 2020-01-08 17:11
 **/
@Data
public class EchoEventMessage extends XWSMsg {
    private JSONObject msg;
}
