package com.ezlinker.app;


import cn.hutool.crypto.SecureUtil;
import com.ezlinker.app.common.exception.XException;
import com.ezlinker.app.common.utils.AliyunEmailUtil;
import com.ezlinker.app.emqintegeration.monitor.EMQMonitor;
import com.ezlinker.app.modules.dataentry.model.DeviceData;
import com.ezlinker.app.modules.dataentry.service.DeviceDataService;
import com.ezlinker.app.modules.internalmessage.model.InternalMessage;
import com.ezlinker.app.modules.internalmessage.service.InternalMessageService;
import com.ezlinker.app.modules.module.model.ModuleData;
import com.ezlinker.app.modules.module.service.IModuleService;
import com.ezlinker.app.modules.module.service.ModuleDataService;
import com.ezlinker.app.modules.user.model.User;
import com.ezlinker.app.modules.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AppApplicationTests {

    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    AliyunEmailUtil aliyunEmailUtil;

    //
    @Test
    void sendTemplateMail() throws XException {
        //创建邮件正文
        Context context = new Context();

        context.setVariable("address", "1号车间");
        context.setVariable("name", "安全控制器");

        String emailContent = templateEngine.process("warning", context);
        aliyunEmailUtil.sendHtmlMail("751957846@qq.com", "警告信息", emailContent);
        System.out.println("邮件发送报告测试");
    }

    @Autowired
    IUserService iUserService;

    @Test
    void addUser() {

        User user = new User();
        user.setUsername("ezlinker")
                .setPassword(SecureUtil.md5("password").toUpperCase())
                .setAvatar("ezlinker.png")
                .setEmail("751957846@qq.com")
                .setNickName("EZ-Linker-Big-dick")
                .setPhone("18059150204")
                .setStatus(1)
                .setUserType(1)
                .setUserProfileId(0L);
        iUserService.save(user);
    }

    @Test
    void getConnections() {
        EMQMonitor emqMonitor = new EMQMonitor("dec4f6b4added", "MjkwNTQyMTc3NjUyNjk5NDQzMjkzNTU3NzcxMzI1OTMxNTC", 8080, "112.74.44.130");
        String connections = emqMonitor.getClusterConnections(1, 10);
        System.out.println(connections);
    }


    @Resource
    IModuleService iModuleService;


    @Resource
    InternalMessageService internalMessageService;

    /**
     * 添加测试站内信
     */
    @Test
    void addInternalMessage() {
        for (int i = 0; i < 52; i++) {
            InternalMessage internalMessage = new InternalMessage();
            internalMessage.setUserId(1L).setContent("hello world").setTitle("fuck you:" + i + " times").setMarked(0).setType(1).setCreateTime(new Date());
            internalMessageService.save(internalMessage);
        }


    }


    @Resource
    DeviceDataService deviceDataService;

    @Test
    void addTestDeviceData() {

        for (int i = 0; i < 348; i++) {
            DeviceData deviceData = new DeviceData();
            Map<String, Object> map = new HashMap<>();
            map.put("K1", i % 2 == 0);
            map.put("K2", i % 5 == 0);
            deviceData.setDeviceId(11L).setCreateTime(new Date()).setData(map);
            deviceDataService.save(deviceData);
        }
    }
    @Resource
    ModuleDataService moduleDataService;
    @Test

    void addTestModuleData() {

        for (int i = 0; i < 348; i++) {
            ModuleData moduleData = new ModuleData();
            Map<String, Object> map = new HashMap<>();
            map.put("K1", i % 2 == 0);
            map.put("K2", i % 5 == 0);
            moduleData.setModuleId(6L).setCreateTime(new Date()).setData(map);
            moduleDataService.save(moduleData);
        }
    }
}
