package com.ezlinker.app.modules.device.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ezlinker.app.common.model.XEntity;
import com.ezlinker.app.modules.device.pojo.FieldParam;
import com.ezlinker.app.modules.feature.model.Feature;
import com.ezlinker.app.modules.module.model.Module;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 实际设备，是产品的一个实例。
 * </p>
 *
 * @author wangwenhai
 * @since 2020-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ez_device")
public class Device extends XEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 项目
     */
    private Long projectId;

    /**
     * 产品
     */
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

    private LocalDateTime lastActive;

    /**
     * 1:在线;2:离线;3未激活
     */
    private Integer state;

    /**
     * 运行的状态
     */
    private String statuses;

    /**
     * MQTT用户名
     */
    private String username;

    /**
     * MQTT ClientID
     */
    private String clientid;

    /**
     * MQTT密码
     */
    private String password;

    /**
     * 是否超级权限
     */
    private Integer isSuperuser;

    /**
     * 认证token
     */
    private String token;

    /**
     * 参数
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FieldParam> parameters;

    /**
     * 描述
     */
    private String description;

    /**
     * 模块
     */
    @TableField(exist = false)
    private List<Module> modules;
    @TableField(exist = false)
    List<Feature> features;
    @TableField(exist = false)
    Set<String> tags;

    public static final String PROJECT_ID = "project_id";

    public static final String PRODUCT_ID = "product_id";

    public static final String NAME = "name";

    public static final String LOGO = "logo";

    public static final String LOCATION = "location";

    public static final String MODEL = "model";

    public static final String INDUSTRY = "industry";

    public static final String SN = "sn";

    public static final String TYPE = "type";

    public static final String LAST_ACTIVE = "last_active";

    public static final String STATE = "state";

    public static final String STATUSES = "statuses";

    public static final String USERNAME = "username";

    public static final String CLIENTID = "clientid";

    public static final String PASSWORD = "password";

    public static final String IS_SUPERUSER = "is_superuser";

    public static final String TOKEN = "token";

    public static final String PARAMETERS = "parameters";

    public static final String DESCRIPTION = "description";

}
