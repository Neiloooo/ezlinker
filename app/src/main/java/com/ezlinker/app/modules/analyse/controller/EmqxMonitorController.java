package com.ezlinker.app.modules.analyse.controller;

import com.ezlinker.app.common.XController;
import com.ezlinker.emqintegeration.monitor.EMQMonitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * ezlinker
 *
 * @author wangwenhai
 * @description EMQX监控器
 * @create 2019-12-17 22:06
 **/
@RestController
@RequestMapping("/monitor/emqx")
public class EmqxMonitorController extends XController {
    /**
     * emqx:
     * host: localhost
     * tcp-port: 1883
     * api-port: 8080
     *
     * @return
     */
    @Value("${emq.host}")
    String host;
    @Value("${emq.apiport}")
    Integer apiPort;
    @Value("${emq.appid}")
    String appid;
    @Value("${emq.appscret}")
    String secret;

    @Bean
    public EMQMonitor emqMonitor() {
        return new EMQMonitor(appid, secret, apiPort, host);
    }

    @Resource
    EMQMonitor emqMonitor;

    public EmqxMonitorController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }







}
