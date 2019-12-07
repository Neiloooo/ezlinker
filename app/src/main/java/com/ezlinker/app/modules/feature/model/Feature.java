package com.ezlinker.app.modules.feature.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.app.modules.feature.pojo.CmdValue;
import com.ezlinker.common.model.XEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 产品的功能（特性），和设备是多对多的关系，通过中间表关联起来
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "ez_feature", autoResultMap = true)
public class Feature extends XEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 名称
     */
    @NotEmpty(message = "名称不可为空值")
    private String name;

    /**
     * 标签
     */
    private String label;
    /**
     * 类型
     */
    @NotEmpty(message = "类型不可为空值")

    private String type;

    /**
     * 产品ID
     */
    @NotNull(message = "产品不可为空值")

    private Integer productId;

    /**
     * 命令Key
     */
    @NotEmpty(message = "指令名不可为空值")
    private String cmdKey = "cmdKey";

    /**
     * 命令Value
     */
    @NotEmpty(message = "指令内容不可为空")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<CmdValue> cmdValue;

    @TableField(exist = false)
    private Long moduleId;

}
