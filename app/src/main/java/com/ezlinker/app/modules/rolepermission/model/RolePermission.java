package com.ezlinker.app.modules.rolepermission.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.app.common.model.XEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 角色和权限关联表
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "ez_role_permission",autoResultMap = true)
public class RolePermission extends XEntity {

    private static final long serialVersionUID=1L;

    private Integer roleId;

    private Integer permissionId;

    /**
     * 用户可允许的METHODS
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> allow;


}
