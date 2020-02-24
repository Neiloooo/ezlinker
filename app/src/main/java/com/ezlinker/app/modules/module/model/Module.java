package com.ezlinker.app.modules.module.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.app.common.model.XEntity;
import com.ezlinker.app.modules.module.pojo.DataArea;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备上面的模块，和设备是多对一关系
 * </p>
 *
 * @author wangwenhai
 * @since 2020-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ez_module")
public class Module extends XEntity {

    private static final long serialVersionUID=1L;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 协议
     */
    private Long protocol;

    /**
     * 类型,1:内部使用,比如EZlinker代理,2:正常客户端
     */
    private Integer type;

    /**
     * 状态:0离线,1在线
     */
    private Integer status;

    /**
     * 名称
     */
    private String name;

    /**
     * 型号
     */
    private String model;

    private Date lastActiveTime;

    /**
     * 数据域
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<DataArea> dataAreas;

    /**
     * 描述
     */
    private String description;


    public static final String DEVICE_ID = "device_id";

    public static final String PROTOCOL = "protocol";

    public static final String CLIENT_ID = "client_id";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String TYPE = "type";

    public static final String STATUS = "status";

    public static final String NAME = "name";

    public static final String MODEL = "model";

    public static final String LAST_ACTIVE_TIME = "last_active_time";

    public static final String SN = "sn";

    public static final String TOKEN = "token";

    public static final String IS_SUPERUSER = "is_superuser";

    public static final String DATA_AREAS = "data_areas";

    public static final String DESCRIPTION = "description";

}
