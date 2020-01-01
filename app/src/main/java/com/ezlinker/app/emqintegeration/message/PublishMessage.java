package com.ezlinker.app.emqintegeration.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * @program: ezlinker
 * @description: 消息发送成功
 * @author: wangwenhai
 * @create: 2019-11-21 11:12
 **/
@Data
@EqualsAndHashCode(callSuper = false)

public class PublishMessage extends EMQWebHookMessage {
    private String action;
    @NotEmpty(message = "from_client_id can't null")
    private String from_client_id;
    private String from_username;
    private String topic;
    private int qos;
    private boolean retain;
    private String payload;
    private long ts;

}
