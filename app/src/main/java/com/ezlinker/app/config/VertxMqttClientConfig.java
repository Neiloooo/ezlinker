package com.ezlinker.app.config;

import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.impl.MqttClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ezlinker
 *
 * @author wangwenhai
 * @description VertX mqtt Client
 * @create 2019-12-16 22:08
 **/
@Configuration
@Slf4j
public class VertxMqttClientConfig {

    /**
     * 构建代理MqttClient
     *
     * @return
     */

    @Bean
    public MqttClientImpl mqttClient() {
        MqttClientOptions mqttClientOptions = new MqttClientOptions();
        return new MqttClientImpl(Vertx.vertx(), mqttClientOptions);
    }

}
