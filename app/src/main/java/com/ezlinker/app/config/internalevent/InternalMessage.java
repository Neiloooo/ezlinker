package com.ezlinker.app.config.internalevent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * @program: ezlinker
 * @description: 内部消息
 * @author: wangwenhai
 * @create: 2020-01-08 10:33
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class InternalMessage extends ApplicationEvent {


    private Integer type;
    private String content;

    public InternalMessage(Object source) {
        super(source);
    }

    public InternalMessage(Object source, Integer type, String content) {
        super(source);
        this.type = type;
        this.content = content;
    }

    @Override
    public String toString() {
        return "InternalMessage{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
