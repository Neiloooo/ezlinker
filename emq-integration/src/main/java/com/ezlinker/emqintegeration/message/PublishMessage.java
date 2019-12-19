package com.ezlinker.emqintegeration.message;

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
    private String messageid;
    private String action;
    @NotEmpty(message = "clientid can't null")

    private String clientid;
    private String username;
    private String topic;
    private int qos;
    private boolean retain;
    private String payload;
    private long timestamp;

    @Override
    public String toString() {
        return "PublishMessage{" +
                "messageid='" + messageid + '\'' +
                ", action='" + action + '\'' +
                ", clientid='" + clientid + '\'' +
                ", username='" + username + '\'' +
                ", topic='" + topic + '\'' +
                ", qos=" + qos +
                ", retain=" + retain +
                ", payload='" + payload + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
