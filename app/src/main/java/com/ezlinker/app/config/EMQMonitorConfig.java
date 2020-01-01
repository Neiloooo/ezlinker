package com.ezlinker.app.config;

import com.ezlinker.app.emqintegeration.monitor.EMQMonitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangwenhai
 * @date 2019/12/28
 * File description: emqx监控工具
 */
@Configuration
public class EMQMonitorConfig {
    /**
     * emqxx:
     * host: localhost
     * tcp-port: 1883
     * api-port: 8080
     *
     * @return
     */
    @Value("${emqx.host}")
    String host;
    @Value("${emqx.apiport}")
    Integer apiPort;
    @Value("${emqx.appid}")
    String appid;
    @Value("${emqx.appscret}")
    String secret;

    @Bean
    public EMQMonitor emqxMonitor() {
        return new EMQMonitor(appid, secret, apiPort, host);
    }

}
