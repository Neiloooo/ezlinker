package com.ezlinker.app.modules.device.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.app.constants.DeviceState;
import com.ezlinker.app.modules.device.pojo.DeviceStatus;
import com.ezlinker.app.modules.device.pojo.FieldParam;
import com.ezlinker.app.modules.feature.model.Feature;
import com.ezlinker.app.modules.module.model.Module;
import com.ezlinker.common.model.XEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 实际设备，是产品的一个实例。
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "ez_device", autoResultMap = true)
public class Device extends XEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 项目
     */
    private Long projectId;

    /**
     * 产品
     */
    @NotNull(message = "产品不可为空值")
    private Long productId;

    /**
     * 名称
     */
    private String name;

    /**
     * Logo
     */
    private String logo;

    /**
     * 地理位置
     */
    private String location;

    /**
     * 型号
     */
    private String model;

    /**
     * 标签
     */
    @TableField(exist = false)
    private Set<String> tags;

    /**
     * 厂家
     */
    private String industry;

    /**
     * 序列号
     */
    private String sn;

    /**
     * 类型
     */
    private String type;

    /**
     * 参数
     */

    @TableField(typeHandler = JacksonTypeHandler.class)

    private List<FieldParam> parameters;


    /**
     * 设备当前状态
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<DeviceStatus> statuses;
    /**
     * 描述
     */
    private String description;


    /**
     * 活跃时间
     */
    private Date lastActive;

    /**
     * 状态
     */
    private Integer state = DeviceState.UN_ACTIVE;

    /**
     * 模块
     */
    @TableField(exist = false)
    private List<Module> modules;
    @TableField(exist = false)

    List<Feature> features;


}
