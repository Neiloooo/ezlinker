package com.ezlinker.app.modules.moduletemplate.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.app.modules.module.pojo.DataArea;
import com.ezlinker.common.model.XEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 产品上面的模块模板
 * </p>
 *
 * @author wangwenhai
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "ez_module_template",autoResultMap = true)
public class ModuleTemplate extends XEntity {

    private static final long serialVersionUID=1L;

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 名称
     */
    private String name;

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


    public static final String PRODUCT_ID = "product_id";

    public static final String NAME = "name";

    public static final String DATA_AREAS = "data_areas";

    public static final String DESCRIPTION = "description";

}
