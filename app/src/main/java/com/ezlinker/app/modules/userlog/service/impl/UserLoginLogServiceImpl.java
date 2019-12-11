package com.ezlinker.app.modules.userlog.service.impl;

import com.ezlinker.app.modules.userlog.model.UserLoginLog;
import com.ezlinker.app.modules.userlog.service.IUserLoginLogService;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户登录日志 服务实现类
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-12
 */
@Service
public class UserLoginLogServiceImpl implements IUserLoginLogService<UserLoginLog> {
    @Resource
    MongoOperations mongoOperations;

    @Override
    public void save(UserLoginLog entity) {
        mongoOperations.insert(entity, "user_login_log");
    }
//
//
//    IPage<UserLoginLog> list(String userId, Pageable pageable) {
//
//        Query query = new Query();
//        Criteria criteria = Criteria.where("userId").is(userId);
//        query.addCriteria(criteria);
//        query.with(pageable);
//        List<UserLoginLog> deviceDataList = mongoOperations.find(query, UserLoginLog.class, "user_login_log");
//        long total = mongoOperations.count(query, UserLoginLog.class);
//        return new Page<UserLoginLog>();
//
//    }
}
