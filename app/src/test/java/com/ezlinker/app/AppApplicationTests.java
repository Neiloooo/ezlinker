package com.ezlinker.app;


import cn.hutool.crypto.SecureUtil;
import com.ezlinker.app.modules.module.model.Module;
import com.ezlinker.app.modules.module.service.IModuleService;
import com.ezlinker.app.modules.user.model.User;
import com.ezlinker.app.modules.user.service.IUserService;
import com.ezlinker.common.exception.XException;
import com.ezlinker.common.utils.AliyunEmailUtil;
import com.ezlinker.emqintegeration.monitor.EMQMonitor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

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

    @Test
    void addEzlinkerMqtttopic() {

        Module ezlinkerWsProxy = new Module();
        ezlinkerWsProxy.setClientId("ezlinker").setIsSuperuser(1).setUsername("ezlinker");
        iModuleService.save(ezlinkerWsProxy);
    }
}
