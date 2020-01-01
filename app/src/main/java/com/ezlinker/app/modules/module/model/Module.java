package com.ezlinker.app.modules.module.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.app.modules.feature.model.Feature;
import com.ezlinker.app.modules.module.pojo.DataArea;
import com.ezlinker.app.modules.mqtttopic.model.MqttTopic;
import com.ezlinker.app.common.model.XEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备上面的模块，和设备是多对一关系
 * 目前此表记录了多种协议的设备数据
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants

@TableName(value = "ez_module", autoResultMap = true)
public class Module extends XEntity {
    public static final long serialVersionUID = 1L;


    /**
     * 内部的客户端
     */
    public static final int INTERNAL = 1;
    /**
     * 设备中断
     */
    public static final int EXTERNAL = 2;

    /**
     * MQTT协议
     */
    public static final int MQTT = 1;
    /**
     * HTTP协议
     */
    public static final int HTTP = 2;
    /**
     * COAP协议
     */
    public static final int COAP = 3;
    /**
     * 原始TCP
     */
    public static final int RAW_TCP = 4;
    /**
     * UDP
     */
    public static final int UDP = 5;


    /**
     * 产品ID
     */
    @NotNull(message = "设备不可为空")
    private Long deviceId;

    /**
     * 类型
     */
    @NotNull(message = "类型不可为空")

    private Integer type;


    /**
     * 名称
     */
    @NotEmpty(message = "模块名不可为空")

    private String name;

    /**
     * 协议
     */
    @NotNull(message = "协议不可为空")

    private Long protocol;

    /**
     * 型号
     */

    private String model;


    /**
     * 序列号
     */
    private String sn;

    /**
     * 密钥，基于计算生成的一个Base64字符串
     */
    private String token;

    /**
     * 是否是超级管理员
     */
    @JsonIgnore
    private Integer isSuperuser;

    /**
     * 数据域
     */
    @NotEmpty(message = "数据域不可为空")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<DataArea> dataAreas;

    /**
     * 描述
     */
    private String description;

    /**
     * MQTT的用户名和密码
     */
    private String clientId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 辅助字段，用来展示该模块支持的功能列表
     */
    @TableField(exist = false)
    private List<Feature> featureList;

    /**
     * 最后在线时间
     */
    private Date lastActiveTime;

    /**
     * 用在详情,辅助性质
     */
    @TableField(exist = false)
    List<MqttTopic> mqttTopics;
}
