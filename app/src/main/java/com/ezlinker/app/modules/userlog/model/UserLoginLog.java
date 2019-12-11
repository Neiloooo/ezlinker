package com.ezlinker.app.modules.userlog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UserLoginLog {

    private String id;
    @JsonIgnore
    private Long userId;

    private String status;

    private String ip;

    private String remark;

    private String location;


}
