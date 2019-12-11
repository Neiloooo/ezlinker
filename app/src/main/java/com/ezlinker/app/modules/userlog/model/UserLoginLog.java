package com.ezlinker.app.modules.userlog.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户登录日志
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-12
 */
@Data
@Accessors(chain = true)
//@TableName("ez_user_login_log")
public class UserLoginLog {

    private String id;
    private Long userId;

    private String status;

    private String ip;

    private String remark;

    private String location;


}
