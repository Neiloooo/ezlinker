package com.ezlinker.app.constants;

/**
 * @program: ezlinker
 * @description: Mqtt的类型:类型 1：S2C；2：C2S；3：STATUS；4：GROUP
 * @author: wangwenhai
 * @create: 2019-12-17 16:43
 **/
public interface MqttTopicType {
    Integer S2C = 1;
    Integer C2S = 2;
    Integer STATUS = 3;
    Integer GROUP = 4;

}
