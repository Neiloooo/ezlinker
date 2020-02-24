package com.ezlinker.app.modules.dataentry.controller;

import com.alibaba.fastjson.JSONObject;
import com.ezlinker.app.common.exception.XException;
import com.ezlinker.app.common.exchange.R;
import com.ezlinker.app.modules.device.service.IDeviceService;
import com.ezlinker.app.modules.module.service.IModuleService;
import com.ezlinker.app.modules.module.service.ModuleLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @program: ezlinker
 * @description: EMQ web hook插件的回调
 * @author: wangwenhai
 * @create: 2019-12-25 16:23
 **/
@RestController
@RequestMapping("/data")
@Slf4j
public class WebHookController {
    @Resource
    ApplicationContext applicationContext;
    @Resource
    IModuleService iModuleService;

    @Resource
    ModuleLogService moduleLogService;

    @PostMapping("/webHook")
    public R publish(@RequestBody @Valid JSONObject message) throws XException {
        // 此处过滤EZLinker,直接放行
//        String clientId = message.getString("clientid");
//        if (clientId == null) {
//            clientId = message.getString("from_client_id");
//        }
//        if (clientId.startsWith("ezlinker")) {
//            return new R();
//        }

        /**
         * 筛选消息类型
         */
        String action = message.getString("action");
        switch (action) {
            case "client_connected":
                handConnect(message.getString("clientid"));
                break;
            case "client_disconnected":
                handDisconnect(message.getString("clientid"));
                break;
            case "message_publish":
                // handMessage(message);
                break;
            default:
                break;
        }
        return R.ok();
    }

    /**
     * 连接处理
     *
     * @param clientId
     * @throws XException
     */

    @Resource
    IDeviceService iDeviceService;
    //TODO 此处应该把模块和设备合为一个查询，避免多次查库。后期优化

    private void handConnect(String clientId) throws XException {
        log.info("客户端[{}]连接成功", clientId);


    }

    /**
     * 客户端离线
     *
     * @param clientId
     * @throws XException
     */
    private void handDisconnect(String clientId) throws XException {
        log.info("客户端[{}]离线", clientId);
//        applicationContext.publishEvent(new InternalMessage(clientId, 0, clientId));
//        Module module = iModuleService.getOne(new QueryWrapper<Module>().eq("client_id", clientId));
//        if (module == null) {
//            throw new XException("Module not exist", "模块不存在");
//        }
//        Device device = iDeviceService.getById(module.getDeviceId());
//
//        module.setStatus(0);
//        iModuleService.updateById(module);
//        // 保存日志
//        ModuleLog moduleLog = new ModuleLog();
//        moduleLog.setSn(device.getSn());
//        moduleLog.setDeviceName(device.getName());
//        moduleLog.setModuleName(module.getName());
//        moduleLog.setModuleId(module.getId());
//        moduleLog.setType(ModuleLog.DISCONNECT);
//        moduleLog.setCreateTime(new Date());
//        moduleLogService.save(moduleLog);

    }

    /**
     * 消息到达
     *
     * @param message
     */
    private void handMessage(JSONObject message) {

    }
}
