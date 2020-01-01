package com.ezlinker.app.modules.scheduleinfo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.app.config.quartz.ScheduleData;
import com.ezlinker.app.common.model.XEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wangwenhai
 * @since 2019-12-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "ez_schedule_info", autoResultMap = true)
public class ScheduleInfo extends XEntity {


    private static final long serialVersionUID = 1L;

    private Long linkId;
    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务组名称
     */
    private String taskGroup;

    /**
     * 触发器名称
     */
    private String triggerName;

    /**
     * 触发器组
     */
    private String triggerGroup;

    /**
     * 表达式
     */
    private String triggerCronExpression;

    /**
     * 目标执行类类名
     */
    private String executeClassName;

    /**
     * 执行类的具体执行方法
     */
    private String executeMethodName;

    /**
     * 数据目标所在表集合","分割用于统计
     */
    private String targetTable;

    /**
     * 是否启动
     */
    private Boolean isStart;

    /**
     * 0删，1允正常
     */
    private String status;

    /**
     * 创建人id
     */
    private String updatedId;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 计划任务的指令
     */
    @NotEmpty(message = "数据域不可为空")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<ScheduleData> scheduleDataList;

    public static final String TASK_DESCRIPTION = "task_description";

    public static final String TASK_NAME = "task_name";

    public static final String TASK_GROUP = "task_group";

    public static final String TRIGGER_NAME = "trigger_name";

    public static final String TRIGGER_GROUP = "trigger_group";

    public static final String TRIGGER_CRON_EXPRESSION = "trigger_cron_expression";

    public static final String EXECUTE_CLASS_NAME = "execute_class_name";

    public static final String EXECUTE_METHOD_NAME = "execute_method_name";

    public static final String TARGET_TABLE = "target_table";

    public static final String IS_START = "is_start";

    public static final String STATUS = "status";

    public static final String UPDATED_ID = "updated_id";

    public static final String UPDATED_TIME = "updated_time";

}
