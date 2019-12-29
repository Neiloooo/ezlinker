package com.ezlinker.app.modules.dataentry.model;

/**
 * ezlinker
 *
 * @author wangwenhai
 * @description 专门给EMQX的WebHook 提供的一个接口,用来一次性查出模块和设备的基本数据
 * @create 2019-12-29 22:35
 **/
public class WebHookInterfaceInfo {
    /**
     * 模块ID
     */
    private Long moduleId;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     *  设备SN
     */
    private String sn;
    /**
     * 模块名
     */
    private String moduleName;
    /**
     * 设备名
     */
    private String deviceName;
}
