package com.ezlinker.app.config;

import com.ezlinker.app.config.mqtt.MqttProxyClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ezlinker
 * @description: Mqtt代理
 * @author: wangwenhai
 * @create: 2019-12-17 10:26
 **/
@Configuration
public class MqttProxyClientConfig {
    @Value("${emqx.host}")
    String emqxHost;
    @Value("${emqx.tcp-port}")
    Integer tcpPort;

    @Bean
    public MqttProxyClient mqttClient() throws MqttException {

        return new MqttProxyClient("tcp://" + emqxHost + ":" + tcpPort, "ezlinker_proxy_6&IJhY&ou68iJUA");
    }
}
