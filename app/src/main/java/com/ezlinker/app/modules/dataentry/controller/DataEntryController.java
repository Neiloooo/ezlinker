package com.ezlinker.app.modules.dataentry.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ezlinker.app.modules.module.model.Module;
import com.ezlinker.app.modules.module.model.ModuleLog;
import com.ezlinker.app.modules.module.service.IModuleService;
import com.ezlinker.app.modules.module.service.ModuleLogService;
import com.ezlinker.app.common.exception.XException;
import com.ezlinker.app.common.exchange.R;
import com.ezlinker.app.emqintegeration.message.ConnectedMessage;
import com.ezlinker.app.emqintegeration.message.DisconnectedMessage;
import com.ezlinker.app.emqintegeration.message.PublishMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

/**
 * @program: ezlinker
 * @description: EMQ数据入口
 * @author: wangwenhai
 * @create: 2019-11-21 10:39
 **/
@RestController
@RequestMapping("/data")
public class DataEntryController {

    @Resource
    IModuleService iModuleService;

    @Resource
    ModuleLogService moduleLogService;

    /**
     * 上线回调
     *
     * @param message
     * @return
     * @throws XException
     */
    @PostMapping("/connected")
    public R connected(@RequestBody @Valid ConnectedMessage message) throws XException {
        System.out.println("设备 Clientid is:" + message.getClient_id() + " Username is:" + message.getUsername() + " 上线");
        if (message.getClient_id().startsWith("ezlinker")) {
            return new R();
        }
        Module module = iModuleService.getOne(new QueryWrapper<Module>().eq("client_id", message.getClient_id()));
        if (module == null) {
            throw new XException("Module not exist", "模块不存在");
        }

        iModuleService.updateById(module);
        // 保存日志
        ModuleLog moduleLog = new ModuleLog();
        moduleLog.setModuleId(module.getId());
        moduleLog.setType(ModuleLog.CONNECT);
        moduleLog.setCreateTime(new Date());
        moduleLogService.save(moduleLog);
        return new R();
    }

    /**
     * 模块离线回调
     *
     * @param message
     * @return
     * @throws XException
     */
    @PostMapping("/disconnected")
    public R disconnected(@RequestBody @Valid DisconnectedMessage message) throws XException {
        if (message.getClient_id().startsWith("ezlinker")) {
            return new R();
        }
        System.out.println("设备 Clientid is:" + message.getClient_id() + " Username is:" + message.getUsername() + " 下线");
        Module module = iModuleService.getOne(new QueryWrapper<Module>().eq("client_id", message.getClient_id()));
        if (module == null) {
            throw new XException("Module not exist", "模块不存在");
        }
        // 保存日志
        ModuleLog moduleLog = new ModuleLog();
        moduleLog.setModuleId(module.getId());
        moduleLog.setType(ModuleLog.DISCONNECT);
        moduleLog.setCreateTime(new Date());
        moduleLogService.save(moduleLog);

        return new R();
    }

    @PostMapping("/publish")
    public R publish(@RequestBody @Valid PublishMessage message) {
        if (message.getFrom_client_id().startsWith("ezlinker")) {
            return new R();
        }
        Module module = iModuleService.getOne(new QueryWrapper<Module>().eq("client_id", message.getFrom_client_id()));
        System.out.println("RMQ Data:" + message);
        return new R();
    }


}
