package com.ezlinker.app.modules.analyse.controller;

import com.alibaba.fastjson.JSONObject;
import com.ezlinker.app.common.XController;
import com.ezlinker.app.common.exchange.R;
import com.ezlinker.app.emqintegeration.monitor.EMQMonitor;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Resource
    EMQMonitor emqMonitor;

    public EmqxMonitorController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    @GetMapping("/brokers")
    public R brokers(){
        String body = emqMonitor.getBrokers();
        if (body!=null && body.length()>10){
            return data(JSONObject.parseObject(body).getJSONArray("data"));
        }
        return fail();
    }






}
