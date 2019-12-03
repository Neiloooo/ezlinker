package com.ezlinker.app.modules.permission.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.common.model.XEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 用户权限
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "ez_permission", autoResultMap = true)
public class Permission extends XEntity {

    private static final long serialVersionUID = 1L;

    private String label;

    private String name;

    private String resource;

    private Integer type;

    private Integer parent;

    private String description;

    /**
     * 给用户授权的HTTP方法,使用类型转换器处理
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> methods;

}
