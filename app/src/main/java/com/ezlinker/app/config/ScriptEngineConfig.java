package com.ezlinker.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptEngineManager;

/**
 * ezlinker
 *
 * @author wangwenhai
 * @description 脚本引擎配置
 * @create 2019-12-08 23:16
 **/
@Configuration
public class ScriptEngineConfig {
    @Bean
    ScriptEngineManager scriptEngineManager() {
        return new ScriptEngineManager();
    }
}
