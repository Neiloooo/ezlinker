package com.ezlinker.app.config;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptEngineManager;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    /**
     * 配置Javascript 脚本引擎
     *
     * @return
     */
    @Bean(name = "jsSandBox")
    NashornSandbox jsSandBox() {
        NashornSandbox sandbox = NashornSandboxes.create();
        sandbox.setMaxCPUTime(100);
        //50M内存
        sandbox.setMaxMemory(50 * 1024);
        sandbox.allowNoBraces(false);
        sandbox.setMaxPreparedStatements(30);
        sandbox.setExecutor(new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), r -> new Thread()));
        return sandbox;
    }
}
