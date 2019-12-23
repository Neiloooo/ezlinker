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
    List<MqttTopic> listByDevice(Long deviceId);

}
