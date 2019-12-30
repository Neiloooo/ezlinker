package com.ezlinker.app.modules.mqtttopic.service;

import com.ezlinker.app.modules.mqtttopic.model.MqttTopic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * MQTT的TOPIC记录 服务类
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-18
 */
public interface IMqttTopicService extends IService<MqttTopic> {
    /**
     * 根据模块查
     * @param moduleId
     * @return
     */
    List<MqttTopic> listByModule(Long moduleId);

    /**
     * 根据设备查Topic
     * @param deviceId
     * @return
     */
    List<MqttTopic> listByDevice(Long deviceId);

}
