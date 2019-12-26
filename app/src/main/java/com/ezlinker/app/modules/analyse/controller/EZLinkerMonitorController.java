package com.ezlinker.app.modules.analyse.controller;

import com.ezlinker.app.common.XController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ezlinker
 *
 * @author wangwenhai
 * @description EZLinker监控
 * @create 2019-12-17 21:21
 **/
@RestController
@RequestMapping("/monitor/ezlinker")
public class EZLinkerMonitorController extends XController {

    public EZLinkerMonitorController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }
}
